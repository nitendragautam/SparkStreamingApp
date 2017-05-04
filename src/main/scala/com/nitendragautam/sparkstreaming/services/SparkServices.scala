package com.nitendragautam.sparkstreaming.services

import java.util.HashMap

import kafka.serializer.StringDecoder
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}
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
val conf = new SparkConf().setAppName("SparkStreamingApp")

    val ssc = new StreamingContext(conf,Seconds(3))

    val kafkaTopic="ndsloganalytics_raw_events"
    val consumerTopic = List(kafkaTopic).toSet

    // need to use the hostname:port for Kafka brokers, not Zookeeper
    val kafkaParams = Map[String,String]("metadata.broker.list" -> "192.168.184.131:9092,192.168.184.131:9093,192.168.184.131:9094");


    val directKafkaStream =
      KafkaUtils.createDirectStream[String,
        String,
        StringDecoder,StringDecoder] (ssc,kafkaParams,consumerTopic);

    directKafkaStream.foreachRDD(rdd=>
      rdd.foreachPartition(part =>
        part.foreach(record =>
//Send Logs to Kafka Topic
          processKafkaRecords(record._2,ssc.sparkContext)


        ))
)




    ssc.start()
    ssc.awaitTermination()
  }



  /*
  Process Kafka Records and sends it to Kafka Topic
   */

  def processKafkaRecords(kafkaRecord: String ,ssc :SparkContext): Unit ={
    val producerTopic ="ndsloganalytics_processed_events"
    //Gets
    val kafkaSink = ProducerSinkBroadcast.getProdBroadCast(ssc)
/*
TODO Process the Access Logs Records Here using Access Logs Parser
TODO and Send it to Kafka processed events Topic
 */


    kafkaSink.value.send(producerTopic,kafkaRecord)

logger.info("SparkStreaming App Record "+kafkaRecord)

  }


}


