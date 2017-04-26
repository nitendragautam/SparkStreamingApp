package com.nitendragautam.sparkstreaming.services
import java.util

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}



/**
  * Kafka Sink Producer which produces messages in
  * Kafka
  */
class ProducerSink(createProducer :() => KafkaProducer[String,String]) extends Serializable {

  lazy val producer = createProducer()

  def send(topic: String,  value: String): Unit = producer.send(new ProducerRecord(topic, value))

}

object ProducerSink {
  def apply(config :util.HashMap[String,Object]) : ProducerSink = {
    val createProducerFunc = () => {
      val producer = new KafkaProducer[String, String](config)
      sys.addShutdownHook {
        producer.close()
      }
      producer
    }
    new ProducerSink(createProducerFunc)
  }}
