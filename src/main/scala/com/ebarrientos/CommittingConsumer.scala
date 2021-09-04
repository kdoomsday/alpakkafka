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

/** Consumes from the topic and commits offsets as it gets them
  */
object CommittingConsumer extends App {
  implicit val system     = ActorSystem("ConsumerSystem")
  implicit val ec         = system.dispatcher

  private val InsertThreads = 3

  val kafkaConsumerSettings =
    ConsumerSettings(system, new StringDeserializer, new StringDeserializer)
      .withBootstrapServers(bootstrapServers)
      .withGroupId("myGroup")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
      .withStopTimeout(0.seconds)

  val control =
    Consumer
      .atMostOnceSource(
        kafkaConsumerSettings,
        Subscriptions.topics(Constants.topic)
      )
      .map(msg => mapper.readValue(msg.value(), classOf[Listing]))
      .filter(listing => listing.price >= 100000)
      .mapAsyncUnordered(InsertThreads)(listing => PostgresDao.insertListing(listing))
      .map(rows => println(s"Inserted $rows row(s)"))
      .toMat(Sink.ignore)(Consumer.DrainingControl.apply)
      .run()

}
