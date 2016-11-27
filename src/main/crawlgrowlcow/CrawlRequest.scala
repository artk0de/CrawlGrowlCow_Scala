package crawlgrowlcow

import java.net.URI


import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.JsonAST._
import org.json4s.native.JsonMethods._

case class CrawlRequest(
                         url: String,
                         extractor: String,
                         method: String = "GET",
                         headers: Option[Map[String, String]] = None,
                         passData: Option[Map[String, String]] = None,
                         requestBody: Option[String] = None,
                         cache: Boolean = true) {

  def toJsonString = compact(render(toJson))

  def toJson = ("url" -> url) ~
    ("extractor" -> extractor) ~
    ("method" -> method) ~
    ("headers" -> headers) ~
    ("requestBody" -> requestBody) ~
    ("passData" -> passData) ~
    ("cache" -> cache)

  lazy val id = {
    val uri = new URI(url)
    val sb = new scala.collection.mutable.StringBuilder
    sb ++= uri.getPath
    if (uri.getQuery != null) {
      sb ++= "?"
      sb ++= uri.getQuery
    }
    for (body <- requestBody) {
      sb ++= body
    }
  }
}

/**
  * Created by art2rik1 on 21.11.16.
  */

