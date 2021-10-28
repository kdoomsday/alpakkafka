package com.ebarrientos.util

import scala.util.Random

object RandUtil {
  /** @param min Minimum wanted value
    * @param max Maximum wanted value
    * @return An int x such that {{{min <= x <= max}}}
    */
  def intInRange(min: Int, max: Int): Int = {
    if (min == max)
      min
    else if (min > max)
      intInRange(max, min)
    else
      Random.nextInt(max - min) + min
  }
}

