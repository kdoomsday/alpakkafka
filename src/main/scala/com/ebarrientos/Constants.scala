package com.ebarrientos

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object Constants {
  val bootstrapServers = "localhost:29092"
  val topic            = "mytopic"

  val mapper =
    new ObjectMapper()
      .registerModule(new DefaultScalaModule)
}
