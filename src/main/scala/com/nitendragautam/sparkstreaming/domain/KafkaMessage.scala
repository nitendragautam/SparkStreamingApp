package com.nitendragautam.sparkstreaming.domain


case class KafkaMessage (dateTime :String ,
                         clientIpAddress :String ,
                         httpStatusCode :String,
                        httpRequestField :String)
