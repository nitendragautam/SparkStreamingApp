###Spark Streaming Job Which reads from kafka

Follow the steps given below 


(a) Make sure the metadata broker list("metadata.broker.list") and Kafka topic is 
updated for your use case

(b)To build the jar file go to root folder of the application and type


sbt assembly

It will create the jar file named "sparkstreamingjob.jar" as given in 
build.sbt

(c)Submit the Spark Job in the spark Cluster






