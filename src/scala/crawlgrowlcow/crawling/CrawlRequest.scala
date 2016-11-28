package scala.crawlgrowlcow.crawling

import java.net.URI

import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._



case class CrawlRequest(url: String,
                        method: String = "GET",
                        headers: Option[Map[String, String]] = None,
                        requestBody: Option[String] = None,
                        cache: Boolean = true) {

  def toJsonString = compact(render(toJson))

  def toJson =  ("url", url) ~
    ("method", method) ~
    ("headers", headers) ~
    ("requestBody", requestBody) ~
    ("cache", cache)

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



