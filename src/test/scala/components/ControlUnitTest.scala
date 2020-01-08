// Unit tests for the main control logic

package dinocpu.test.components

import chisel3._
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import dinocpu.components.Control

class ControlUnitTester(c: Control) extends PeekPokeTester(c) {
  private val ctl = c


}

/**
  * This is a trivial example of how to run this Specification
  * From within sbt use:
  * {{{
  * testOnly dinocpu.ControlTester
  * }}}
  * From a terminal shell use:
  * {{{
  * sbt 'testOnly dinocpu.ControlTester'
  * }}}
  */
class ControlTester extends ChiselFlatSpec {
  "Control" should s"match expectations" in {
    Driver(() => new Control) {
      c => new ControlUnitTester(c)
    } should be (true)
  }
}
