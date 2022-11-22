import chisel3._
import chisel3.iotesters._
import chisel3.stage.ChiselStage
import chisel3.util._
import scala.io.Source
import ascon._
import permutation._


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
  for (j <- 0 until 40) {
    step(1)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Tag: " + peek(dut.io.tagout).toString(16))  
    //println("State: " + peek(dut.io.state).toString(16) + " Warning: " + peek(dut.io.warning).toString(16))   
  }
  step(1)
  poke(dut.io.empty, true.B)
  poke(dut.io.message, "h80000000000000000000000000000000".U)
  println("Push: " + peek(dut.io.push).toString
  (16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Tag: " + peek(dut.io.tagout).toString(16))  

  for (j <- 0 until 16) {
    step(1)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Tag: " + peek(dut.io.tagout).toString(16))  
  }
  step(1)
  poke(dut.io.message, "h000102030405060708090A0B0C0D0E0F".U)
  println("Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Tag: " + peek(dut.io.tagout).toString(16))  

  for (j <- 0 until 16) {
    step(1)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Tag: " + peek(dut.io.tagout).toString(16))  
  }
  step(1)
  poke(dut.io.full, true.B)
  poke(dut.io.message, "h80000000000000000000000000000000".U)
  println("Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Tag: " + peek(dut.io.tagout).toString(16))  

  for (j <- 0 until 60) {
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
  for (j <- 0 until 40) {
    step(1)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Valid: " + peek(dut.io.valid).toString(16))  
    //println("State: " + peek(dut.io.state).toString(16) + " Warning: " + peek(dut.io.warning).toString(16))   
  }
  step(1)
  poke(dut.io.empty, true.B)
  poke(dut.io.message, "h80000000000000000000000000000000".U)
  println("Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Valid: " + peek(dut.io.valid).toString(16))  
  
  for (j <- 0 until 16) {
    step(1)
    poke(dut.io.message, "h00000000000000000000000000000000".U)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Valid: " + peek(dut.io.valid).toString(16))    
  }
  step(1)
  poke(dut.io.message, "h52499ac9c84323a4ae24eaeccf45c137".U)
  println("Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Valid: " + peek(dut.io.valid).toString(16))

  for (j <- 0 until 16) {
    step(1)
    poke(dut.io.message, "h00000000000000000000000000000000".U)
    println(j + "Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Valid: " + peek(dut.io.valid).toString(16))   
  }
  step(1)
  poke(dut.io.full, true.B)
  poke(dut.io.message, "h0000000000000000000000000000000".U)
  println("Push: " + peek(dut.io.push).toString(16) + " Pull: " + peek(dut.io.pull).toString(16) + " Done: " + peek(dut.io.done).toString(16) + " Cipher: " + peek(dut.io.cipher).toString(16) + " Valid: " + peek(dut.io.valid).toString(16))  

  for (j <- 0 until 60) {
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
  for (j <- 0 until 40) {
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
  for (j <- 0 until 22) {
    step(1)
    println("Done: " + peek(dut.io.done).toString() + " Result is: " + peek(dut.io.s_out).toString(16))
  }
  step(1)
  poke(dut.io.s_in, "h00400c00000000000000000000000000000000000000000000000000000000000000000000000000".U)
  poke(dut.io.start, true.B)
  println("*Done: " + peek(dut.io.done).toString() + " Result is: " + peek(dut.io.s_out).toString(16))
  step(1)
  poke(dut.io.start, false.B)
  for (j <- 0 until 23) {
    step(1)
    println("Done: " + peek(dut.io.done).toString() + " Result is: " + peek(dut.io.s_out).toString(16))
  }
}

object newBehavior extends App {
  chisel3.iotesters.Driver(() => new permutation_new()) { c =>
    new newBehavior(c)
  }
}