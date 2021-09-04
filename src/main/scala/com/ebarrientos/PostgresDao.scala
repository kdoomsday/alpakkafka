package com.ebarrientos

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

/** Dao for working with the postgres database */
object PostgresDao {

  val db = Database.forConfig("postgres")

  def insertListing(listing: Listing): Future[Int] = {
    val q = sqlu"""INSERT INTO Listing(name, price, address)
                   VALUES(${listing.name}, ${listing.price}, ${listing.address})"""
    db.run(q)
  }
}
