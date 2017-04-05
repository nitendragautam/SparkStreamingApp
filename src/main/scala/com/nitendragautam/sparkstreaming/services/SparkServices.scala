package com.nitendragautam.sparkstreaming.services

import kafka.serializer.{DefaultDecoder, StringDecoder}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.slf4j.{Logger, LoggerFactory}

/**
  * Spark Utility Classes.
  */
class SparkServices {

  private val logger: Logger =
    LoggerFactory.getLogger(classOf[SparkServices])

  def startSparkStreamingCluster(){
val conf =
  new SparkConf().setAppName("SparkStreamingApp")

    val ssc = new StreamingContext(conf,Seconds(10))
    val kafkaTopic="testTopic"
    // need to use the hostname:port for Kafka brokers, not Zookeeper
    val kafkaParams = Map[String,String]("metadata.broker.list" -> "192.168.184.131:9092,192.168.184.131:9093,192.168.184.131:9094");
val topic = List(kafkaTopic).toSet



    val directKafkaStream =
      KafkaUtils.createDirectStream[String,
        String,
        StringDecoder,StringDecoder] (ssc,kafkaParams,topic);

    directKafkaStream.foreachRDD(rdd=>
rdd.foreach(record =>
           logger.info(record._2)
)
)




    ssc.start()
    ssc.awaitTermination()
  }


}
