package com.nitendragautam.sparkstreaming.main

import com.nitendragautam.sparkstreaming.services.SparkServices

/**
*Main Entry
 */
object Boot {
  def main(args: Array[String]) {
val sr = new SparkServices
sr.startSparkStreamingCluster()

  }
}
