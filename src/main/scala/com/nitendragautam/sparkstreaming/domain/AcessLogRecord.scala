package com.nitendragautam.sparkstreaming.domain

/*
Domain Class for Access Logs
 */
case class AccessLogRecord(
                            clientAddress :String, //ip address or hostName of the client
                            clientIdentity :String, //Generally '-'
                            remoteUser :String , //generally '-'
                            dateTime :String, //Format [day/month/year:hour:second zone]
                            httpRequest :String , //'GET /request
                            httpStatusCode :String , //200 ,404 ,500 etc
                            bytesSent :String, //some value or '-'
                            siteReferer :String,     //Visitors Source
                            userAgent: String   //Browser User Agent
                          )
