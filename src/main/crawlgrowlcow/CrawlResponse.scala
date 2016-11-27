package crawlgrowlcow

import java.net.URI

import scala.xml.XML
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.JsonAST._
import org.json4s.native.JsonMethods._
import org.jsoup.Jsoup


/**
  * Created by art2rik1 on 21.11.16.
  */
case class CrawlResponse(
                     request: CrawlRequest,
                     status: Int,
                     headers: Map[String, List[String]],
                     body: String,
                     created: Long = System.currentTimeMillis,
                     timeTaken: Int = 1
                   ) {
  def toDom = Jsoup.parse(body)
  def toJson = parse(body).asInstanceOf[JObject]
  def toXml = XML.loadString(body)
  def toJsonString = compact(render(("request" -> request.toJson) ~
    ("status" -> status) ~ ("headers" -> headers) ~
    ("response" -> body) ~ ("created" -> created) ~
    ("timeTaken" -> timeTaken)))
}



