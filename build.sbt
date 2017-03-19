
organization := "com.nitendragautam"

version := "1.0"

scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

mainClass in assembly := Some("com.nitendragautam.sparkstreaming.main.ApplicationBoot")
assemblyJarName in assembly := "sparkstreamingjob.jar"
libraryDependencies ++= {
val sparkV     =  "2.0.1"

  
  Seq(
    "org.apache.spark" % "spark-streaming_2.11" % sparkV % "provided",
    "org.apache.spark" % "spark-core_2.11" % sparkV % "provided",
    "org.apache.spark" % "spark-streaming-kafka-0-8_2.11" % sparkV,
    "log4j" % "log4j" % "1.2.17"

  )}

// To avoid warnings on Meta-Inf merges, where the default is to take the first, and..
// spark-streaming-kafka has a dependency on org.spark-project.spark unused 1.0.0 but also embeds
// the jar in its artifact, which causes a problem using assembly to create the Spark fatjar
assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("org","apache","spark","unused","UnusedStubClass.class") => MergeStrategy.first
  case x => (assemblyMergeStrategy in assembly).value(x)
}