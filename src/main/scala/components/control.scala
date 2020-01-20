// Control logic for the processor

package dinocpu.components

import chisel3._
import chisel3.util.{BitPat, ListLookup}

/**
 * Main control logic for our simple processor
 *
 * Input: opcode:     Opcode from instruction
 *
 * Output: branch     true if branch or jump and link (jal). update PC with immediate
 * Output: memread    true if we should read from memory
 * Output: memwrite   true if writing to the data memory
 * Output: memtoreg   true if writing data from memory to the register file
 * Output: aluop      00 for ld/st, 10 for R-type, 01 for branch
 * Output: regwrite   true if writing to the register file
 * Output: alusrc     source for the second ALU input (0 is readdata2 and 1 is immediate)
 * Output: pcadd      Use PC as the input to the ALU
 * Output: bypassalu  Bypass the ALU and just use the value from immediate
 * Output: pcreg      Update PC with the register value (e.g., for jalr)
 * Output: itype      True if we're working on an itype instruction
 * Output: jump       True if we want to update the PC with pc+imm regardless of the ALU result
 * Output: pcfromalu  Use the pc from the ALU, not pc+4 or pc+imm
 * Output: validinst  True if the instruction we're decoding is valid
 *
 * For more information, see section 4.4 of Patterson and Hennessy.
 * This follows figure 4.22.
 */

class Control extends Module {
  val io = IO(new Bundle {
    val opcode = Input(UInt(7.W))

    val branch = Output(Bool())
    val memread = Output(Bool())
    val memwrite = Output(Bool())
    val memtoreg = Output(Bool())
    val aluop = Output(UInt(2.W))
    val regwrite = Output(Bool())
    val alusrc = Output(Bool())
    val pcadd = Output(Bool())
    val bypassalu = Output(Bool())
    val pcreg = Output(Bool())
    val itype = Output(Bool())
    val jump = Output(Bool())
    val pcfromalu = Output(Bool())
    val validinst = Output(Bool())
  })

  val signals =
    ListLookup(io.opcode,
      /*default*/           List(false.B, false.B, false.B, false.B,   0.U,   false.B,  false.B, false.B, false.B,   false.B, false.B, false.B, false.B,   false.B),
      Array(              /*     branch,  memread, memwrite, memtoreg, aluop, regwrite, alusrc,  pcadd,   bypassalu, pcreg,   itype,   jump,    pcfromalu, validinst */
      // R-format
      BitPat("b0110011") -> List(false.B, false.B, false.B, false.B,   2.U,   true.B,   false.B, false.B, false.B,   false.B, false.B, false.B, false.B,   true.B),

      // Your code goes here for Lab 2.
      // Remember to make sure to have commas at the end of each line

      ) // Array
    ) // ListLookup

  io.branch := signals(0)
  io.memread := signals(1)
  io.memwrite := signals(2)
  io.memtoreg := signals(3)
  io.aluop := signals(4)
  io.regwrite := signals(5)
  io.alusrc := signals(6)
  io.pcadd := signals(7)
  io.bypassalu := signals(8)
  io.pcreg := signals(9)
  io.itype := signals(10)
  io.jump := signals(11)
  io.pcfromalu := signals(12)
  io.validinst := signals(13)
}
