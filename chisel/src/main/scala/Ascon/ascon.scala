// See README.md for license details.

package ascon

import chisel3._
import chisel3.iotesters._
import chisel3.stage.ChiselStage
import chisel3.util._
import scala.io.Source
import permutation._
import layers._


class ascon extends Module {
  val io = IO(new Bundle {
    val key = Input(UInt(128.W))
    val nounce = Input(UInt(128.W))
    val tagin  = Input(UInt(128.W))

    val message = Input(UInt(128.W))
    val start   = Input(Bool())
    val empty   = Input(Bool())
    val full    = Input(Bool())
    val mode    = Input(UInt(3.W)) // 0 encryption ascon, 1 encryption ascona, 2 decryption ascon, 3 decryption ascona, 4 hash, 5 hasha, 

    val push    = Output(Bool())
    val pull    = Output(Bool())
    val cipher  = Output(UInt(128.W))
    val tagout  = Output(UInt(128.W))
    val done    = Output(Bool())
    val warning = Output(Bool())
    val valid = Output(Bool())
    // val state  = Output(UInt(320.W))
  })
  val idle :: initial :: absorb :: transit :: squeeze :: fin :: Nil = Enum(6)
  // The state register
  val initReg = RegInit(false.B)
  val stateReg = RegInit(idle)
  val r    = Wire(UInt(8.W))
  val b    = Wire(UInt(8.W))
  val rReg    = Wire(UInt(8.W))
  val bReg    = Wire(UInt(8.W))
  val kReg = RegInit(0.U(128.W))
  val modeReg = RegInit(0.U(3.W))
  val tagReg = RegInit(0.U(128.W))
  val tagoutReg = RegInit(0.U(128.W))

  val head_update = Wire(UInt(128.W))
  val init_update = Wire(UInt(128.W))
  val tail_update = Wire(UInt(320.W))
  val permut_outReg = RegInit(0.U(320.W))

  val end = Wire(Bool())
  val endReg =  RegInit(false.B)
  val doneReg = RegInit(false.B)

  val permutation = Module(new permutation_two_wrapper())

  // io.state := permutation.io.s_in

  val warningReg = RegInit(0.U(1.W))

  r              := Mux(io.mode(0) && ~io.mode(2), 128.U, 64.U)
  b              := Mux(io.mode(0), 8.U, 
                    Mux(~io.mode(0) && io.mode(2), 12.U, 6.U))
  rReg           := Mux(modeReg(0) && ~modeReg(2), 128.U, 64.U)
  bReg           := Mux(modeReg(0), 8.U, 
                    Mux(~modeReg(0) && modeReg(2), 12.U, 6.U))

  warningReg     := (stateReg =/= idle) && (modeReg =/= io.mode)
  io.warning     := warningReg

  permut_outReg := permutation.io.s_out

  head_update := Mux(stateReg===initial || stateReg===absorb || ~modeReg(1) || modeReg(2), io.message, 
                Mux((stateReg===transit || stateReg===squeeze) && ~io.full, io.message ^ (permut_outReg(255,128) >> rReg) ^ permut_outReg(319,192), 
                Mux((stateReg===transit || stateReg===squeeze) && io.full, (1.U << 127.U), 0.U(128.W))))

  init_update := Mux(initReg && ~modeReg(2), kReg, 0.U(128.W))
  
  tail_update := Mux(stateReg===transit && ~io.full && ~modeReg(2), 1.U(320.W),
                 Mux(stateReg===transit && io.full && ~modeReg(2), 1.U(320.W) ^ (kReg << (192.U-rReg)), 
                 Mux(stateReg===squeeze && io.full && ~modeReg(2), kReg << (192.U-rReg), 0.U(320.W))))
  
  doneReg := permutation.io.done


  permutation.io.s_in := Mux(stateReg===idle && io.mode(2), Cat(0.U(8.W), 64.U(8.W), 12.U(8.W), 12.U(8.W)-b, 256.U(32.W), 0.U(256.W)),
                        Mux(stateReg===idle && ~io.mode(2), Cat(128.U(8.W), r, 12.U(8.W), b, 0.U(32.W), io.key, io.nounce), (head_update << 192.U) ^ permut_outReg ^ tail_update ^ init_update))
  
  permutation.io.start := Mux(stateReg===idle, io.start, ((stateReg=/=fin && ~modeReg(2)) || (~io.full && modeReg(2))) && doneReg)

  permutation.io.round := Mux(stateReg===idle || ((stateReg===transit || stateReg===squeeze) && io.full && ~modeReg(2)) || ((stateReg===initial || stateReg===absorb) && io.empty && modeReg(2)), 12.U, bReg)

  io.pull := (stateReg===initial || stateReg===absorb) && doneReg
  io.push := (stateReg===transit || stateReg===squeeze) && doneReg



  io.cipher := Mux(~io.push, 0.U, 
               Mux(modeReg(0) && ~modeReg(2),permut_outReg(319,192) ^ io.message, permut_outReg(319,256) ^ io.message(127, 64)))
  
  end := Mux(modeReg(2), io.push && io.full, stateReg===fin && doneReg)
  endReg := end
  tagoutReg :=  Mux(end, permut_outReg(127,0) ^ kReg, 0.U)
  io.tagout := tagoutReg
  io.done := endReg

  io.valid := endReg && (tagReg===tagoutReg)

  switch (stateReg) {
    is (idle) {
      when(io.start) {
        stateReg := initial
        kReg := io.key
        modeReg := io.mode
        initReg := true.B
        tagReg := io.tagin
      }
    }
    is (initial) {
      when(io.empty && doneReg && modeReg(2)) {
        stateReg := squeeze
      }.elsewhen(io.empty && ~modeReg(2)) {
        stateReg := transit
        initReg := ~doneReg
      }.elsewhen(doneReg && ~io.empty) {
        stateReg := absorb
        initReg := false.B
      }
    }
    is (absorb) {
      when(doneReg && modeReg(2)) {
        stateReg := squeeze
      }.elsewhen(doneReg && ~modeReg(2)) {
        stateReg := transit
      }
    }
    is (transit) {
      when(doneReg && io.full) {
        initReg := false.B
        stateReg := fin
      }.elsewhen(doneReg && ~io.full) {
        initReg := false.B
        stateReg := squeeze
      }
    }
    is (squeeze) {
      when(doneReg && io.full && modeReg(2)) {
        stateReg := idle
      }.elsewhen(doneReg && io.full && ~modeReg(2)) {
        stateReg := fin
      }
    }
    is (fin) {
      when(endReg) {
        stateReg := idle
      }
    }
  }
}