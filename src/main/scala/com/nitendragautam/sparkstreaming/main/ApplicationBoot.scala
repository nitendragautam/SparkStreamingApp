package com.nitendragautam.sparkstreaming.main

import com.nitendragautam.sparkstreaming.services.SparkServices

/**
*Main Entry
 */
object ApplicationBoot {
  def main(args: Array[String]) {
val sr = new SparkServices
sr.startSparkStreamingCluster()

  }
}
