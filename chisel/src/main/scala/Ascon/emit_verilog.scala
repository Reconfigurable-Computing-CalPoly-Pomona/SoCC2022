import chisel3._
import chisel3.iotesters._
import chisel3.stage.ChiselStage
import chisel3.util._
import scala.io.Source
import ascon._
import permutation._
import layers._

object ascon_Driver extends App {
  (new ChiselStage).emitVerilog(new ascon)
}