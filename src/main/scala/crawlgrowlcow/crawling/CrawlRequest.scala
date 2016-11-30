package scala.crawlgrowlcow.crawling

import java.net.URI

import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import scalaj.http._

import java.security.MessageDigest

case class CrawlRequest(url: String,
                        method: String = "GET",
                        headers: Option[Map[String, String]] = None,
                        requestBody: Option[Map[String, String]] = None,
                        cache: Boolean = true) {

  val client: HttpRequest = Http(url).method(method)
  buildRequest()

  /**
    * Build the request
    */
  private def buildRequest() = {
    if (headers.isDefined) {
      client.headers(headers.get)
    }

    if (requestBody.isDefined) {
      client.params(requestBody.get)
    }
  }

  /**
    * Execute a request
    * @return Response
    */
  def execute(): HttpResponse[String] = {
    return client.asString
  }

  def toJsonString = compact(render(toJson))

  def toJson =  ("url", url) ~
    ("method", method) ~
    ("headers", headers.get) ~
    ("requestBody", requestBody.get) ~
    ("cache", cache)
}



