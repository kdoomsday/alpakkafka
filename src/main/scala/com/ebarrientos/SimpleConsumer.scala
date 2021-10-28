package com.ebarrientos

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.ConsumerSettings
import akka.kafka.Subscriptions
import akka.kafka.scaladsl.Consumer
import akka.stream.scaladsl.Keep
import akka.stream.scaladsl.Sink
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.duration._

import Constants._

/** Consumes from the topic and does not commit
  */
object SimpleConsumer extends App {
  implicit val system = ActorSystem("ConsumerSystem")
  implicit val ec     = system.dispatcher

  val kafkaConsumerSettings =
    ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers(bootstrapServers)
      .withGroupId("myGroup")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
      .withStopTimeout(0.seconds)

  val control =
    Consumer
      .committableSource(
        kafkaConsumerSettings,
        Subscriptions.topics(Constants.topic)
      )
      .map(msg => mapper.readValue(msg.record.value(), classOf[Listing]))
      .map { msg =>
        println(s"Precio: ${msg.price}, M2=${msg.m2}")
        msg
      }
      .filter(listing => listing.price >= 100000)
      .mapAsyncUnordered(3)(listing => PostgresDao.insertListing(listing))
      .recover{ case ex =>
        ex.printStackTrace()
        0
      }
      .map(rows => println(s"Inserted $rows row(s)"))
      .toMat(Sink.ignore)(Consumer.DrainingControl.apply)
      .run()

  control.streamCompletion.onComplete { _ =>
    system.terminate()
    println(s"All done")
  }
}
