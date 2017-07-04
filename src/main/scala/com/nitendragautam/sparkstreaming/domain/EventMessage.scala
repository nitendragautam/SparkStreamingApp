package com.nitendragautam.sparkstreaming.domain


case class EventMessage(dateTime :String,
                        clientIpAddress :String,
                        httpStatusCode :String,
                        httpRequestField :String,
                        httpRequestBytes :String)
