###Spark Streaming Job Which reads from kafka topic

Follow the steps given below 


(a) Make sure the metadata broker list("metadata.broker.list") and Kafka topic is 
updated for your use case

(b)To build the jar file go to root folder of the application and type


sbt assembly

It will create the jar file named "sparkstreamingjob.jar" as given in 
build.sbt

(c)Submit the Spark Streaming Job in the spark Cluster.Make sure the
Spark cluster and Kafka is running


$SPARK_HOME/bin/spark-submit --class "com.nitendragautam.sparkstreaming.main.Boot" --master spark://192.168.184.131:7077 /home/hduser/sparkstreamingjob.jar



Go to the port 8080 from browser to check the spark job that you submitted.
For my case it is 192.168.184.131:8080



