package com.ebarrientos

import akka.kafka.ProducerSettings
import akka.actor.ActorSystem
import akka.serialization.StringSerializer
import akka.serialization.StringSerializer
import org.apache.kafka.common.serialization.StringSerializer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord
import akka.kafka.scaladsl

import scala.util.{Failure, Random, Success}
import Constants._
import akka.Done
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import com.fasterxml.jackson.databind.ObjectMapper
import akka.stream.scaladsl.Sink

object SimpleProducer extends App {
  implicit val system = ActorSystem("KafkaSystem")
  implicit val ec     = system.dispatcher

  val kafkaProducerSettings =
    ProducerSettings(system, new StringSerializer, new StringSerializer)
      .withBootstrapServers(bootstrapServers)

  val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri =  s"http://localhost:8080/price?lat=${Random.between(0,100)}&long=${Random.between(0,100)}"))
  val responseAsString:Future[String] = responseFuture.mapTo[String]
  /** Producir un mensaje aleatorio */
  private def mkMessage(): Listing = {
    val l1 = Listing.randomListing()
     responseAsString.onComplete {
      case Success(res) => Listing(l1.name, res.toInt, l1.address, l1.m2)
      case Failure(_)   => l1
    }
  }



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
