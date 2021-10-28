package com.ebarrientos.util

import org.scalatest.flatspec.AnyFlatSpec

class RandUtilTest extends AnyFlatSpec {
  implicit val iterations = 100

  /** Test for a given range */
  private def testRange(min: Int, max: Int)(implicit iterations: Int) = {
    val genList =
      for {
        _        <- 1 to iterations
        generated = RandUtil.intInRange(min, max)
      } yield generated

    assert(genList.forall(x => x >= min && x <= max))
  }

  "RandUtil" should "generate an int in range" in {
    testRange(10, 20)
  }

  it should "generate an int in range for negatives" in {
    testRange(-100, -20)
  }

  it should "generate an int in range for ranges that change symbol" in {
    testRange(-10, 10)
  }

  it should "work when passing args in the wrong order" in {
    val (min, max) = (0, 10)
    val res        = RandUtil.intInRange(max, min)
    assert(res >= min)
    assert(res <= max)
  }

  it should "Give the exact number if min == max" in {
    assert(RandUtil.intInRange(0, 0) === 0)
  }
}
