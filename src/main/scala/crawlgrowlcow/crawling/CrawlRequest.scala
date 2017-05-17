package scala.crawlgrowlcow.crawling

import java.net.URI

import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import scalaj.http._

import java.security.MessageDigest

class CrawlRequest(url_p: String,
                        cache: Boolean = true) {

  val url: String = url_p
  val client: HttpRequest = Http(url)

  /**
    * Execute a request
    * @return Response
    */
  def execute(): HttpResponse[String] = {
    return client.asString
  }

  def toJsonString = compact(render(toJson))

  def toJson =  ("url", url) ~
    ("method", client.method) ~
    ("headers", client.headers) ~
    ("requestBody", client.params) ~
    ("cache", cache)
}



