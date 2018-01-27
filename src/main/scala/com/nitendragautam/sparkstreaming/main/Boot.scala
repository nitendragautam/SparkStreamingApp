package com.nitendragautam.sparkstreaming.main

import com.nitendragautam.sparkstreaming.services.SparkServices

/**
*Main Entry To the Application
 */
object Boot {
  def main(args: Array[String]) {
val sr = new SparkServices
sr.startSparkStreamingCluster()

  }
}
