package edu.ndsu.sparkstreaming.services

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

  def startSparkCluster(masterConfig :String){
val conf =
  new SparkConf().setAppName("KafkaTest")

    val ssc = new StreamingContext(conf,Seconds(10))

    val kafkaParams = Map[String,String]("metadata.broker.list" -> "192.168.184.131:9093");
    val topics = List(("inputMessage", 1)).toMap
val topic = Set("inputMessage")

val dataStream =
  KafkaUtils
    .createStream(ssc ,"192.168.184.131:2181","inputMessageGroup",topics)

    dataStream.foreachRDD(rdd =>{
      logger.info("Created Kafka Stream ")
      logger.info("Converting RDD to String "+rdd.toString())
      logger.info("Rdd Count  "+rdd.count())
      logger.info("Rdd Count  "+rdd.values.toString())
      logger.info("Rdd Count  "+rdd.values.toString())
    })
    logger.info("Data Stream Printing "+ dataStream.print())

/*
    val directKafkaStream =
      KafkaUtils.createDirectStream[String,
        Array[Byte],
        StringDecoder,DefaultDecoder] (ssc,kafkaParams,topic);

    directKafkaStream.foreachRDD(rdd=>{
      logger.info("Created Kafka Stream ")
      logger.info("Converting RDD to String "+rdd.toString())
      logger.info("Rdd Count  "+rdd.count())
      logger.info("Rdd Count  "+rdd.values.toString())
      logger.info("Rdd Count  "+rdd.values.toString())
})

*/


    ssc.start()
    ssc.awaitTermination()
  }


}
