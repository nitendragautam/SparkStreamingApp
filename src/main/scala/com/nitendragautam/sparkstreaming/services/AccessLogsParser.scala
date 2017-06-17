package com.nitendragautam.sparkstreaming.services

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.regex.{Matcher, Pattern}

import com.nitendragautam.sparkstreaming.domain.AccessLogRecord

import scala.util.control.Exception.allCatch

/*
Logic for Parsing Access Logs
 */
class AccessLogsParser extends Serializable{
  //Regex for Logs Data

  private val ddd = "\\d{1,3}"        //At least 1 but not more than 3 times eg:192 or 92
  private val clientIpAddress = s"($ddd\\.$ddd\\.$ddd\\.$ddd)?"   //Eg 192.168.138.1
  private val clientIdentity = "(\\S+)"   //  '\S' is non whitespace character
  private val remoteUser  = "(\\S+)"     //Any non white Space Character
  private val dateTime = "(\\[.+?\\])"     //Eg :`[21/Jun/2010:08:45:13 -0700]`
  private val httpRequest = "\"(.*?)\""    //any number of any characters
  private val httpStatus = "(\\d{3})"
  private val requestBytes = "(\\S+)"     //Can be empty or '-'
  private val siteReferer = "\"(.*?)\""  //Site Referer
  private val userAgent  = "\"(.*?)\""    //User Agents
  private val accessLogsRegex = s"$clientIpAddress $clientIdentity $remoteUser $dateTime $httpRequest $httpStatus $requestBytes $siteReferer $userAgent"
  private val pattern =Pattern.compile(accessLogsRegex)


  /*
  Parses Access Logs by passing Single Line
  As group(0) is for entire record ,we skip this group
  Input is logRecord  in the combined log format
  output obtained is the Access Record instance wrapped in Option
*/
  def parseAccessLogs(logRecord :String): Option[AccessLogRecord] ={

    val regexMatcher=pattern.matcher(logRecord)
    if(regexMatcher.find){ //If Pattern in matched
      Some(buildAccessLogRecord(regexMatcher))

    }else{
      None //No Pattern is Matched
    }
  }

  /*
  Parses the record but returns null object version of Access LogRecord
  Input value is the Access Log Record in combined log format
Returns Null version of Access Logs Instance if the parsing of the log fails .
Fields obtained in the Null Object wil be empty strings
   */

  def parseNullRecordsonFailure(logRecord :String): AccessLogRecord ={
    val patternMatcher = pattern.matcher(logRecord)
    if(patternMatcher.find){
      buildAccessLogRecord(patternMatcher)
    }else{ //If No matches Found
      AccessLogRecord("","","","","","","","","") //Null Access Record
    }

  }
  //Group
  private def buildAccessLogRecord(matcher :Matcher)={
    AccessLogRecord(
      matcher.group(1),
      matcher.group(2),
      matcher.group(3),
      matcher.group(4),
      matcher.group(5),
      matcher.group(6),
      matcher.group(7),
      matcher.group(8),
      matcher.group(9)
    )
  }

  /**
  passed Paramatere :Http Request Field like "GET /history/skylab/skylab-small.gif HTTP/1.0"
    *returns a Tuple of (requestType ,uri ,httpVersion)
    */

  def parseHttpRequestField(httpRequest :String):Option[Tuple3[String,String,String]] ={
    val splittedArray= httpRequest.split(" ") //Split the Request based on Space
    if (splittedArray.size == 3)
      Some((splittedArray(0),splittedArray(1),splittedArray(2)))
    else
      None
  }

  /*
  Parses the Date Field    "[21/Jun/2010:02:48:13 -0700]"
  and gets the Time in Long
   */
  def parseDateField(dateField :String): Option[Long] ={
    val dateFormat ="\\[(.*?) .+]"
    val datePattern =Pattern.compile(dateFormat) //Using Regex to compile the pattern
    val dateMatcher =datePattern.matcher(dateField)
    if(dateMatcher.find){
      val dateString = dateMatcher.group(1) //Match the Date
      println(" Date "+dateString)

      val dateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss",Locale.ENGLISH)
      allCatch.opt(dateFormat.parse(dateString).getTime)  //Returns Option [Date] //Catches All Exception
    }else{
      None
    }
  }

}
