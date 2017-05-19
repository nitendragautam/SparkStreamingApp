package com.nitendragautam.sparkstreaming.services

import java.util.HashMap


import kafka.serializer.StringDecoder
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.slf4j.{Logger, LoggerFactory}

/**
  * Spark Services
  */
class SparkServices extends Serializable{
val accessLogsParser = new AccessLogsParser
  private val logger: Logger =
    LoggerFactory.getLogger(classOf[SparkServices])

  def startSparkStreamingCluster(){
val conf = new SparkConf().setAppName("SparkStreamingApp")

     val ssc = new StreamingContext(conf,Seconds(3))

    val props = getProducerProperties() //
    val kafkaSink = ssc.sparkContext.broadcast(ProducerSink(props)) //Broadcasting Kafka Sink
    val kafkaTopic="ndsloganalytics_raw_events"
    val producerTopic ="ndsloganalytics_processed_events"
    val consumerTopic = List(kafkaTopic).toSet

    // need to use the hostname:port for Kafka brokers, not Zookeeper
    val kafkaParams = Map[String,String]("metadata.broker.list" -> "192.168.133.128:9092,192.168.133.128:9093,192.168.133.128:9094");


    val directKafkaStream =
      KafkaUtils.createDirectStream[String,
        String,
        StringDecoder,StringDecoder] (ssc,kafkaParams,consumerTopic);

    directKafkaStream.foreachRDD(rdd=>

      rdd.foreachPartition(part =>
        part.foreach(record => {
          // Process the Kafka Records Send to Kafka Topic
          val processedRecords =accessLogsParser.parseAccessLogs(record._2).toString

          kafkaSink.value.sendMessageToKafka(producerTopic,processedRecords)
logger.info("message sent to Kafka " +processedRecords)
        }
        ))
)




    ssc.start()
    ssc.awaitTermination()
  }


  def getProducerProperties(): HashMap[String, Object] ={
    val props = new HashMap[String, Object]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.133.128:9093")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    props
  }
}


