package layers

import chisel3._
import chisel3.iotesters._
import chisel3.stage.ChiselStage
import chisel3.util._
import scala.io.Source




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


// single round permutation
class permutation extends Module {
  val io = IO(new Bundle {
    val round_in        = Input(UInt(8.W))
    val x_in        = Input(Vec(5, UInt(64.W)))
    val x_out = Output(Vec(5, UInt(64.W)))
  })

    val addition = Module(new addition_layer())
    val substitution = Module(new substitution_layer())
    val diffusion= Module(new diffusion_layer())
    val substitution_reg = Reg(Vec(5, UInt(64.W)))

    addition.io.round_in := io.round_in
    addition.io.x2_in := io.x_in(2)

    substitution.io.x_in(0) := io.x_in(0)
    substitution.io.x_in(1) := io.x_in(1)
    substitution.io.x_in(2) := addition.io.x2_out
    substitution.io.x_in(3) := io.x_in(3)
    substitution.io.x_in(4) := io.x_in(4)

    substitution_reg(0) := substitution.io.x_out(0)
    substitution_reg(1) := substitution.io.x_out(1)
    substitution_reg(2) := substitution.io.x_out(2)
    substitution_reg(3) := substitution.io.x_out(3)
    substitution_reg(4) := substitution.io.x_out(4)
  
    diffusion.io.x_in(0) := substitution_reg(0)
    diffusion.io.x_in(1) := substitution_reg(1)
    diffusion.io.x_in(2) := substitution_reg(2)
    diffusion.io.x_in(3) := substitution_reg(3)
    diffusion.io.x_in(4) := substitution_reg(4)

    io.x_out(0) := diffusion.io.x_out(0)
    io.x_out(1) := diffusion.io.x_out(1)
    io.x_out(2) := diffusion.io.x_out(2)
    io.x_out(3) := diffusion.io.x_out(3)
    io.x_out(4) := diffusion.io.x_out(4) 
}