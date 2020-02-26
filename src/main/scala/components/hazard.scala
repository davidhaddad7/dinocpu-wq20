// This file contains the hazard detection unit

package dinocpu.components

import chisel3._

/**
 * The hazard detection unit
 *
 * Input:  rs1, the first source register number
 * Input:  rs2, the second source register number
 * Input:  idex_memread, true if the instruction in the ID/EX register is going to read from memory
 * Input:  idex_rd, the register number of the destination register for the instruction in the ID/EX register
 * Input:  exmem_taken, if true, then we are using the nextpc in the EX/MEM register, *not* pc+4.
 *
 * Output: pcfromtaken, if true, use the pc from MEM
 * Output: pcstall, if true, stall the pipeline
 * Output: ifid_bubble, if true, we should instet a bubble in the IF/ID stage
 * Output: idex_bubble, if true, we should insert a bubble in the ID/EX stage
 * Output: exmem_bubble, if true, we should insert a bubble in the EX/MEM stage
 * Output: ifid_flush, if true, set the IF/ID register to 0
 *
 * For more information, see Section 4.7 and beginning of 4.8 of Patterson and Hennessy
 * This follows the "Data hazards and stalls" section and the "Assume branch not taken" section
 */
class HazardUnit extends Module {
  val io = IO(new Bundle {
    val rs1          = Input(UInt(5.W))
    val rs2          = Input(UInt(5.W))
    val idex_memread = Input(Bool())
    val idex_rd      = Input(UInt(5.W))
    val exmem_taken  = Input(Bool())

    val pcwrite      = Output(UInt(2.W))
    val pcfromtaken  = Output(Bool())
    val pcstall      = Output(Bool())
    val ifid_bubble  = Output(Bool())
    val idex_bubble  = Output(Bool())
    val exmem_bubble = Output(Bool())
    val ifid_flush   = Output(Bool())
  })

  // default
  io.pcwrite      := 0.U
  io.pcfromtaken  := false.B
  io.pcstall      := false.B
  io.ifid_bubble  := false.B
  io.idex_bubble  := false.B
  io.exmem_bubble := false.B
  io.ifid_flush   := false.B

  // Load to use hazard.
  when (io.idex_memread &&
        (io.idex_rd === io.rs1 || io.idex_rd === io.rs2)) {
    io.pcfromtaken := false.B
    io.pcstall     := true.B
    io.ifid_bubble := true.B
    io.idex_bubble := true.B
  }

  // branch flush
  when (io.exmem_taken) {
    io.pcfromtaken  := true.B // use the PC from mem stage
    io.pcstall      := false.B // use the PC from mem stage
    io.ifid_flush   := true.B
    io.idex_bubble  := true.B
    io.exmem_bubble := true.B
  }
}
