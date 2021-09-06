package com.ebarrientos

import org.scalatest.flatspec.AnyFlatSpec
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.ScalaObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

class ListingMapperTest extends AnyFlatSpec {
  "ListingMapper" should "map to string" in {
    val listing    = Listing("nombre", BigDecimal(101000), "direccion", 75)
    val mapper     = new ObjectMapper().registerModule(new DefaultScalaModule)
    val listingStr = mapper.writeValueAsString(listing)


    assert(listingStr.length() > "{}".length())
    assert(listingStr.contains("nombre"))
  }
}
