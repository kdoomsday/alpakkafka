package com.ebarrientos

import scala.util.Random
import com.fasterxml.jackson.annotation.JsonProperty

final case class Listing(name: String, price: BigDecimal, address: String)

object Listing {

  def randomListing(): Listing = {
    val name    = Random.nextString(10)
    val price   = BigDecimal(Random.nextInt(1000000))
    val address = Random.nextString(30)
    Listing(name, price, address)
  }
}
