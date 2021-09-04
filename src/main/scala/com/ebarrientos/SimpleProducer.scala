package com.ebarrientos

import akka.kafka.ProducerSettings
import akka.actor.ActorSystem
import akka.serialization.StringSerializer
import akka.serialization.StringSerializer
import org.apache.kafka.common.serialization.StringSerializer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord
import akka.kafka.scaladsl
import scala.util.Random
import Constants._
import akka.Done
import scala.concurrent.Future
import com.fasterxml.jackson.databind.ObjectMapper
import akka.stream.scaladsl.Sink

object SimpleProducer extends App {
  implicit val system = ActorSystem("KafkaSystem")
  implicit val ec     = system.dispatcher

  val kafkaProducerSettings =
    ProducerSettings(system, new StringSerializer, new StringSerializer)
      .withBootstrapServers(bootstrapServers)

  /** Producir un mensaje aleatorio */
  private def mkMessage() = Listing.randomListing()


  private def toJsonStr(listing: Listing): String =
    mapper.writeValueAsString(listing)


  val done: Future[Done] =
    Source (
      (1 to Random.nextInt(10))
        .map(_ => mkMessage())
    )
      .map { listing =>
        val listingStr = toJsonStr(listing)
        println(listingStr)
        new ProducerRecord[String, String](topic, listingStr)
      }
      .runWith(scaladsl.Producer.plainSink(kafkaProducerSettings))

  done
    .map { d =>
      println("Finished producing")
      d
    }
    .onComplete(_ => system.terminate())
}
