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

object SimpleProducer extends App {
  implicit val system = ActorSystem("KafkaSystem")
  implicit val ec = system.dispatcher


  val kafkaProducerSettings =
    ProducerSettings(system, new StringSerializer, new StringSerializer)
      .withBootstrapServers(bootstrapServers)

  val done =
    Source
      .single(s"""{"a": "${Random.nextInt()}"}""")
      .map { elem =>
        new ProducerRecord[String, String](topic, elem)
      }
      .runWith(scaladsl.Producer.plainSink(kafkaProducerSettings))

  done
    .map{ d =>
      println("Finished producing")
      d
    }
    .onComplete(_ => system.terminate())
}
