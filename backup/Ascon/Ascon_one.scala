// See README.md for license details.

package Hash

import chisel3._
import chisel3.iotesters._
import chisel3.stage.ChiselStage
import chisel3.util._
import scala.io.Source

/**

  */
class addition_layer extends Module {
  val io = IO(new Bundle {
    val round_in        = Input(UInt(8.W))
    val x2_in        = Input(UInt(64.W))
    val x2_out = Output(UInt(64.W))
  })
    val array = Wire(Vec(12, UInt(64.W)))
    array(0) := "hf0".U
    array(1) := "he1".U
    array(2) := "hd2".U
    array(3) := "hc3".U
    array(4) := "hb4".U
    array(5) := "ha5".U
    array(6) := "h96".U
    array(7) := "h87".U
    array(8) := "h78".U
    array(9) := "h69".U
    array(10) := "h5a".U
    array(11) := "h4b".U

    io.x2_out := io.x2_in ^ array(io.round_in)
}


class substitution_layer extends Module {
  val io = IO(new Bundle {
    val x_in        = Input(Vec(5, UInt(64.W)))
    val x_out = Output(Vec(5, UInt(64.W)))
  })
  val array = Wire(Vec(32, UInt(5.W)))
  val temp = Wire(Vec(64, UInt(5.W)))
  array(0) := "h4".U
  array(1) := "hb".U
  array(2) := "h1f".U
  array(3) := "h14".U
  array(4) := "h1a".U
  array(5) := "h15".U
  array(6) := "h9".U
  array(7) := "h2".U
  array(8) := "h1b".U
  array(9) := "h5".U
  array(10) := "h8".U
  array(11) := "h12".U
  array(12) := "h1d".U
  array(13) := "h3".U
  array(14) := "h6".U
  array(15) := "h1c".U
  array(16) := "h1e".U
  array(17) := "h13".U
  array(18) := "h7".U
  array(19) := "he".U
  array(20) := "h0".U
  array(21) := "hd".U
  array(22) := "h11".U
  array(23) := "h18".U
  array(24) := "h10".U
  array(25) := "hc".U
  array(26) := "h1".U
  array(27) := "h19".U
  array(28) := "h16".U
  array(29) := "ha".U
  array(30) := "hf".U
  array(31) := "h17".U

  for (i <- 0 until 64) {
    temp(i) := array(Cat(io.x_in(0)(i),io.x_in(1)(i),io.x_in(2)(i),io.x_in(3)(i),io.x_in(4)(i)))
  }

  for (j <- 0 until 5) {
    io.x_out(j) := Cat(temp(63)(4-j), temp(62)(4-j), temp(61)(4-j), temp(60)(4-j), temp(59)(4-j), temp(58)(4-j), temp(57)(4-j), temp(56)(4-j), temp(55)(4-j), temp(54)(4-j), temp(53)(4-j), temp(52)(4-j), temp(51)(4-j), temp(50)(4-j), temp(49)(4-j), temp(48)(4-j), temp(47)(4-j), temp(46)(4-j), temp(45)(4-j), temp(44)(4-j), temp(43)(4-j), temp(42)(4-j), temp(41)(4-j), temp(40)(4-j), temp(39)(4-j), temp(38)(4-j), temp(37)(4-j), temp(36)(4-j), temp(35)(4-j), temp(34)(4-j), temp(33)(4-j), temp(32)(4-j), temp(31)(4-j), temp(30)(4-j), temp(29)(4-j), temp(28)(4-j), temp(27)(4-j), temp(26)(4-j), temp(25)(4-j), temp(24)(4-j), temp(23)(4-j), temp(22)(4-j), temp(21)(4-j), temp(20)(4-j), temp(19)(4-j), temp(18)(4-j), temp(17)(4-j), temp(16)(4-j), temp(15)(4-j), temp(14)(4-j), temp(13)(4-j), temp(12)(4-j), temp(11)(4-j), temp(10)(4-j), temp(9)(4-j), temp(8)(4-j), temp(7)(4-j), temp(6)(4-j), temp(5)(4-j), temp(4)(4-j), temp(3)(4-j), temp(2)(4-j), temp(1)(4-j), temp(0)(4-j))
  }
}



class diffusion_layer extends Module {
  val io = IO(new Bundle {
    val x_in        = Input(Vec(5, UInt(64.W)))
    val x_out = Output(Vec(5, UInt(64.W)))
  })
    
    io.x_out(0) := io.x_in(0) ^ Cat(io.x_in(0)(18,0),io.x_in(0)(63,19)) ^ Cat(io.x_in(0)(27,0),io.x_in(0)(63,28))
    io.x_out(1) := io.x_in(1) ^ Cat(io.x_in(1)(60,0),io.x_in(1)(63,61)) ^ Cat(io.x_in(1)(38,0),io.x_in(1)(63,39))
    io.x_out(2) := io.x_in(2) ^ Cat(io.x_in(2)(0,0),io.x_in(2)(63,1)) ^ Cat(io.x_in(2)(5,0),io.x_in(2)(63,6))
    io.x_out(3) := io.x_in(3) ^ Cat(io.x_in(3)(9,0),io.x_in(3)(63,10)) ^ Cat(io.x_in(3)(16,0),io.x_in(3)(63,17))
    io.x_out(4) := io.x_in(4) ^ Cat(io.x_in(4)(6,0),io.x_in(4)(63,7)) ^ Cat(io.x_in(4)(40,0),io.x_in(4)(63,41))
}



class permutation extends Module {
  val io = IO(new Bundle {
    val round_in        = Input(UInt(8.W))
    val x_in        = Input(Vec(5, UInt(64.W)))
    val round_out = Output(UInt(8.W))
    val x_out = Output(Vec(5, UInt(64.W)))
  })

    val addition = Module(new addition_layer())
    val substitution = Module(new substitution_layer())
    val diffusion= Module(new diffusion_layer())

    addition.io.round_in := io.round_in
    addition.io.x2_in := io.x_in(2)
    io.round_out :=  io.round_in + 1.U


    substitution.io.x_in(0) := io.x_in(0)
    substitution.io.x_in(1) := io.x_in(1)
    substitution.io.x_in(2) := addition.io.x2_out
    substitution.io.x_in(3) := io.x_in(3)
    substitution.io.x_in(4) := io.x_in(4)

    diffusion.io.x_in(0) := substitution.io.x_out(0)
    diffusion.io.x_in(1) := substitution.io.x_out(1)
    diffusion.io.x_in(2) := substitution.io.x_out(2)
    diffusion.io.x_in(3) := substitution.io.x_out(3)
    diffusion.io.x_in(4) := substitution.io.x_out(4)

    io.x_out(0) := diffusion.io.x_out(0)
    io.x_out(1) := diffusion.io.x_out(1)
    io.x_out(2) := diffusion.io.x_out(2)
    io.x_out(3) := diffusion.io.x_out(3)
    io.x_out(4) := diffusion.io.x_out(4) 
}



class permutation_new extends Module {
  val io = IO(new Bundle {
    val s_in        = Input(UInt(320.W))
    val start        = Input(Bool())
    val round        = Input(UInt(4.W))
    val done        = Output(Bool())
    val s_out = Output(UInt(320.W))
  })
    val x0_Reg = RegInit(0.U(64.W))
    val x1_Reg = RegInit(0.U(64.W))
    val x2_Reg = RegInit(0.U(64.W))
    val x3_Reg = RegInit(0.U(64.W))
    val x4_Reg = RegInit(0.U(64.W))
    val current_round = RegInit(0.U(8.W))
    val single_round = Module(new permutation())
    val run = RegInit(0.U(1.W))

    when (run === 0.U) {
      x0_Reg := io.s_in(319,256)
      x1_Reg := io.s_in(255,192)
      x2_Reg := io.s_in(191,128)
      x3_Reg := io.s_in(127,64)
      x4_Reg := io.s_in(63,0)
      current_round := 12.U - io.round
      run := io.start
    }
    .elsewhen (run === 1.U) {
      x0_Reg := single_round.io.x_out(0)
      x1_Reg := single_round.io.x_out(1)
      x2_Reg := single_round.io.x_out(2)
      x3_Reg := single_round.io.x_out(3)
      x4_Reg := single_round.io.x_out(4)
      current_round := single_round.io.round_out
      run := Mux(current_round === 10.U, 0.U, 1.U)
    }

    single_round.io.round_in := current_round
    single_round.io.x_in(0) := x0_Reg
    single_round.io.x_in(1) := x1_Reg
    single_round.io.x_in(2) := x2_Reg
    single_round.io.x_in(3) := x3_Reg
    single_round.io.x_in(4) := x4_Reg


    when (current_round === 11.U) {
      io.done := true.B
      io.s_out := Cat(single_round.io.x_out(0), single_round.io.x_out(1), single_round.io.x_out(2), single_round.io.x_out(3), single_round.io.x_out(4))
    }
    .otherwise {
      io.done := false.B;
      io.s_out := 0.U
    }
  
}


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

  val permutation = Module(new permutation_new())

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


class encryptBehavior(dut:ascon) extends PeekPokeTester(dut) {
  poke(dut.io.key, "h000102030405060708090A0B0C0D0E0F".U)
  poke(dut.io.nounce, "h000102030405060708090A0B0C0D0E0F".U)
  poke(dut.io.message, "h000102030405060708090A0B0C0D0E0F".U)
  poke(dut.io.tagin, "h00000000000000000000000000000000".U)

  poke(dut.io.start, true.B)
  poke(dut.io.empty, false.B)
  poke(dut.io.full,false.B)
  poke(dut.io.mode, 1.U)
  step(1)
  poke(dut.io.start, false.B)
  for (j <- 0 until 20) {
    step(1)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Tag: " + peek(dut.io.tagout).toString(16))  
    //println("State: " + peek(dut.io.state).toString(16) + " Warning: " + peek(dut.io.warning).toString(16))   
  }
  step(1)
  poke(dut.io.empty, true.B)
  poke(dut.io.message, "h80000000000000000000000000000000".U)
  println("Push: " + peek(dut.io.push).toString
  (16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Tag: " + peek(dut.io.tagout).toString(16))  

  for (j <- 0 until 8) {
    step(1)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Tag: " + peek(dut.io.tagout).toString(16))  
  }
  step(1)
  poke(dut.io.message, "h000102030405060708090A0B0C0D0E0F".U)
  println("Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Tag: " + peek(dut.io.tagout).toString(16))  

  for (j <- 0 until 8) {
    step(1)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Tag: " + peek(dut.io.tagout).toString(16))  
  }
  step(1)
  poke(dut.io.full, true.B)
  poke(dut.io.message, "h80000000000000000000000000000000".U)
  println("Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Tag: " + peek(dut.io.tagout).toString(16))  

  for (j <- 0 until 30) {
    step(1)
    poke(dut.io.message, "h00000000000000000000000000000000".U)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Tag: " + peek(dut.io.tagout).toString(16))  
  }
}

object encryptBehavior extends App {
  chisel3.iotesters.Driver(() => new ascon) { c =>
    new  encryptBehavior(c)
  }
}



class decryptBehavior(dut:ascon) extends PeekPokeTester(dut) {
  poke(dut.io.key, "h000102030405060708090A0B0C0D0E0F".U)
  poke(dut.io.nounce, "h000102030405060708090A0B0C0D0E0F".U)
  poke(dut.io.message, "h000102030405060708090A0B0C0D0E0F".U)
  poke(dut.io.tagin, "h316d7ab17724ba67a85ecd3c0457c459".U)
  //poke(dut.io.data, "h8000000000000000".U)
  poke(dut.io.start, true.B)
  poke(dut.io.empty, false.B)
  poke(dut.io.full, false.B)
  poke(dut.io.mode, 3.U)
  step(1)
  poke(dut.io.start, false.B)
  for (j <- 0 until 20) {
    step(1)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Valid: " + peek(dut.io.valid).toString(16))  
    //println("State: " + peek(dut.io.state).toString(16) + " Warning: " + peek(dut.io.warning).toString(16))   
  }
  step(1)
  poke(dut.io.empty, true.B)
  poke(dut.io.message, "h80000000000000000000000000000000".U)
  println("Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Valid: " + peek(dut.io.valid).toString(16))  
  
  for (j <- 0 until 8) {
    step(1)
    poke(dut.io.message, "h00000000000000000000000000000000".U)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Valid: " + peek(dut.io.valid).toString(16))    
  }
  step(1)
  poke(dut.io.message, "h52499ac9c84323a4ae24eaeccf45c137".U)
  println("Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Valid: " + peek(dut.io.valid).toString(16))

  for (j <- 0 until 8) {
    step(1)
    poke(dut.io.message, "h00000000000000000000000000000000".U)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Valid: " + peek(dut.io.valid).toString(16))   
  }
  step(1)
  poke(dut.io.full, true.B)
  poke(dut.io.message, "h0000000000000000000000000000000".U)
  println("Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Valid: " + peek(dut.io.valid).toString(16))  

  for (j <- 0 until 30) {
    step(1)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Valid: " + peek(dut.io.valid).toString(16) + " Tag: " + peek(dut.io.tagout).toString(16))  
  }
}

object decryptBehavior extends App {
  chisel3.iotesters.Driver(() => new ascon) { c =>
    new  decryptBehavior(c)
  }
}


class hashBehavior(dut: ascon) extends PeekPokeTester(dut) {
  poke(dut.io.key, "h00000000000000000000000000000000".U)
  poke(dut.io.nounce, "h00000000000000000000000000000000".U)
  poke(dut.io.tagin, "h00000000000000000000000000000000".U)
  poke(dut.io.message, "h00010203040506070000000000000000".U)
  //poke(dut.io.message, "h8000000000000000".U)
  poke(dut.io.start, true.B)
  poke(dut.io.empty, false.B)
  poke(dut.io.full, false.B)
  poke(dut.io.mode, 5.U)
  step(1)
  poke(dut.io.start, false.B)
  for (j <- 0 until 20) {
    step(1)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Result: " + peek(dut.io.cipher).toString(16))  
    // println("State: " + peek(dut.io.state).toString(16) + " Warning: " + peek(dut.io.warning).toString(16))   
  }
    step(1)
    poke(dut.io.message, "h08800000000000000000000000000000".U)
    //poke(dut.io.message, "h0000000000000000".U)
    poke(dut.io.empty, true.B)
    println("*Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Result: " + peek(dut.io.cipher).toString(16))  
    // println("State: " + peek(dut.io.state).toString(16) + " Warning: " + peek(dut.io.warning).toString(16)) 

  for (j <- 0 until 60) {
    step(1)
    poke(dut.io.message, "h00000000000000000000000000000000".U)
    println("Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Result: " + peek(dut.io.cipher).toString(16))   
    // println("State: " + peek(dut.io.state).toString(16) + " Warning: " + peek(dut.io.warning).toString(16)) 
  }
}

object hashBehavior extends App {
  chisel3.iotesters.Driver(() => new ascon) { c =>
    new  hashBehavior(c)
  }
}



class newBehavior(dut: permutation_new) extends PeekPokeTester(dut) {
  // poke(dut.io.s_in, "hfe9398aadb67f03d8bb21831c60f1002b48a92db98d5da6243189921b8f8e3e8348fa5c9d525e140".U)
  poke(dut.io.s_in, "h00400c00000000000000000000000000000000000000000000000000000000000000000000000000".U)
  poke(dut.io.round, 12.U)
  poke(dut.io.start, true.B)
  step(1)
  poke(dut.io.start, false.B)
  for (j <- 0 until 10) {
    step(1)
    println("Done: " + peek(dut.io.done).toString() + " Result is: " + peek(dut.io.s_out).toString(16))
  }
  step(1)
  poke(dut.io.s_in, "h00400c00000000000000000000000000000000000000000000000000000000000000000000000000".U)
  poke(dut.io.start, true.B)
  println("*Done: " + peek(dut.io.done).toString() + " Result is: " + peek(dut.io.s_out).toString(16))
  step(1)
  poke(dut.io.start, false.B)
  for (j <- 0 until 11) {
    step(1)
    println("Done: " + peek(dut.io.done).toString() + " Result is: " + peek(dut.io.s_out).toString(16))
  }
}

object newBehavior extends App {
  chisel3.iotesters.Driver(() => new permutation_new()) { c =>
    new newBehavior(c)
  }
}

// object addition_Driver extends App {
//   (new ChiselStage).emitVerilog(new addition_layer)
// }

// object substitution_Driver extends App {
//   (new ChiselStage).emitVerilog(new substitution_layer)
// }

// object diffusion_Driver extends App {
//   (new ChiselStage).emitVerilog(new diffusion_layer)
// }

// object permutation_Driver extends App {
//   (new ChiselStage).emitVerilog(new permutation)
// }

// object permutation_new_Driver extends App {
//   (new ChiselStage).emitVerilog(new permutation_new())
// }

object ascon_Driver extends App {
  (new ChiselStage).emitVerilog(new ascon)
}

//cbf1dbda3490e2845c5ea698a0401d7f
//000102030405060708090A0B0C0D0E0F