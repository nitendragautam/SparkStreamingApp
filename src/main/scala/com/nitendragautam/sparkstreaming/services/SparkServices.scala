package com.nitendragautam.sparkstreaming.services

import java.util.HashMap

import kafka.serializer.{DefaultDecoder, StringDecoder}
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.slf4j.{Logger, LoggerFactory}

/**
  * Spark Services
  */
class SparkServices extends Serializable{

  private val logger: Logger =
    LoggerFactory.getLogger(classOf[SparkServices])

  def startSparkStreamingCluster(){
val conf =
  new SparkConf().setAppName("SparkStreamingApp")

    val ssc = new StreamingContext(conf,Seconds(3))

    /*
    Producer Properties
     */

    val props = new HashMap[String, Object]()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.184.131:9093")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      "org.apache.kafka.common.serialization.StringSerializer")

    val kafkaTopic="ndsloganalytics_raw_events"
    val consumerTopic = List(kafkaTopic).toSet
    val producerTopic ="ndsloganalytics_processed_events"

    // need to use the hostname:port for Kafka brokers, not Zookeeper
    val kafkaParams = Map[String,String]("metadata.broker.list" -> "192.168.184.131:9092,192.168.184.131:9093,192.168.184.131:9094");

val kafkaSink = ssc.sparkContext.broadcast(ProducerSink(props))


    val directKafkaStream =
      KafkaUtils.createDirectStream[String,
        String,
        StringDecoder,StringDecoder] (ssc,kafkaParams,consumerTopic);

    directKafkaStream.foreachRDD(rdd=>
      rdd.foreachPartition(part =>
        part.foreach(record =>
//Send Logs to Kafka Topic
          kafkaSink.value.send(producerTopic,record._2)

        ))
)




    ssc.start()
    ssc.awaitTermination()
  }



  /*
  Process Kafka Records
   */

  def processKafkaRecords(kafkaRecord: String): Unit ={
logger.info("SparkStreaming App Record "+kafkaRecord)
  }
}
