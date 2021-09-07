package com.ebarrientos

import scala.concurrent.Await
import scala.concurrent.duration._

object QueryDatabase extends App {
  implicit val ec = scala.concurrent.ExecutionContext.global

  val listings = PostgresDao.queryListings(5)
  val max      = PostgresDao.maxPrice(400)

  Await
    .result(PostgresDao.getNames(), 10.seconds)
    .foreach { case (name, m2) =>
      println(s"La propiedad $name tiene $m2 metros cuadrados")
    }
}
