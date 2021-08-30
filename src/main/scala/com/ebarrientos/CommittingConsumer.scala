package com.ebarrientos

import akka.actor.ActorSystem
import akka.kafka.ConsumerSettings
import org.apache.kafka.common.serialization.StringDeserializer
import Constants._
import org.apache.kafka.clients.consumer.ConsumerConfig
import akka.kafka.scaladsl.Consumer
import akka.kafka.Subscriptions
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Keep
import akka.Done
import scala.concurrent.duration._

/**
  * Consumes from the topic and commits offsets as it gets them
  */
object CommittingConsumer extends App {
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
      .atMostOnceSource(
        kafkaConsumerSettings,
        Subscriptions.topics(Constants.topic)
      )
      .map(msg => println(s">>> ${msg.value()}"))
      .toMat(Sink.ignore)(Consumer.DrainingControl.apply)
      .run()

}
