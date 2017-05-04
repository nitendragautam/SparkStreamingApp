package com.nitendragautam.sparkstreaming.services
import java.util
import java.util.HashMap
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast



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


/*
Using this Singleton Object to register a Producer Sink
Broad Cast Variable
 */
object ProducerSinkBroadcast{
  @volatile private var kafkaSink : Broadcast[ProducerSink] =null
  val props = new HashMap[String, Object]()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.184.131:9093")
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringSerializer")

  def getProdBroadCast(sc :SparkContext): Broadcast[ProducerSink] ={
if(kafkaSink == null){

}
    if (kafkaSink == null) {
      kafkaSink = sc.broadcast(ProducerSink(props))
    }
    kafkaSink
  }

}
