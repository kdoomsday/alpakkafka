package com.ebarrientos

import scala.util.Random
import com.fasterxml.jackson.annotation.JsonProperty
import com.ebarrientos.util.RandUtil

final case class Listing(name: String, price: BigDecimal, address: String, m2: Int) {
  require(m2 > 0)
}

object Listing {

  def randomListing(): Listing = {
    val name    = Random.nextString(10)
    val price   = BigDecimal(Random.nextInt(1000000))
    val address = Random.nextString(30)
    val m2      = RandUtil.intInRange(50, 1000)
    Listing(name, price, address, m2)
  }
}
