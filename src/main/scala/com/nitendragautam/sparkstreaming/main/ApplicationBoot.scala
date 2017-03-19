package com.nitendragautam.sparkstreaming.main

import com.nitendragautam.sparkstreaming.services.SparkServices

/**
*Main Entry
 */
object ApplicationBoot {
  def main(args: Array[String]) {
val sr = new SparkServices

    val masterConfig = "spark://192.168.184.131:7077"

sr.startSparkCluster(masterConfig)

  }
}
