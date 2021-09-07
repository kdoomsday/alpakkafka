package com.ebarrientos

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import slick.jdbc.GetResult
import slick.jdbc.PositionedResult
import scala.concurrent.ExecutionContext

/** Dao for working with the postgres database */
object PostgresDao {

  val db = Database.forConfig("postgres")

  def insertListing(listing: Listing): Future[Int] = {
    println(s"Insertar listing: M2=${listing.m2}, precio=${listing.price}")
    val q = sqlu"""INSERT INTO Listing(name, price, address, m2)
                   VALUES(${listing.name}, ${listing.price}, ${listing.address}, ${listing.m2})"""
    db.run(q)
  }

  def queryListings(n: Int): Future[Iterable[Listing]] = {
    val q = sql"""select "name", price, address, m2 from listing l limit $n;"""
    db.run(q.as[Listing])
  }

  def maxPrice(maxM2: Int)(implicit ec: ExecutionContext): Future[BigDecimal] = {
    db.run(sql"""select max(price) from listing where m2 < $maxM2""".as[BigDecimal])
      .map(_.head)
  }

  def getNames(): Future[Iterable[(String, Int)]] = {
    db.run(sql"""select "name", m2 from listing l""".as[(String, Int)])
  }

  implicit private val getResult: GetResult[Listing] = GetResult {
    result: PositionedResult =>
      Listing(
        result.nextString(),
        result.nextBigDecimal(),
        result.nextString(),
        result.nextInt()
      )
  }
}
