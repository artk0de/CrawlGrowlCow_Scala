package crawlgrowlcow

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ Future, Promise }
import scala.util.{ Failure, Success }
import scala.collection.JavaConversions._
import dispatch._

class DispatchHttp extends Http {
  private val client = new dispatch.Http().configure(_
    .setAllowPoolingConnection(true)
    .setFollowRedirects(true))

  override def crawl(request: CrawlRequest): Future[CrawlResponse] = {
    val crawlResponse = Promise[CrawlResponse]
    val req = buildRequest(request)
    val start = System.currentTimeMillis
    client(req) onComplete {
      case Failure(t) => crawlResponse.failure(t)
      case Success(response) => {
        val headers = (for {
          header <- response.getHeaders.entrySet
        } yield (header.getKey, header.getValue.toList)).toMap
        val timeTaken = (System.currentTimeMillis - start).toInt
        crawlResponse.success(CrawlResponse(request, response.getStatusCode, headers, response.getResponseBody)
      }
    }
    crawlResponse.future
  }

  def buildRequest(request: CrawlRequest): Req = {

    var req = url(request.url)

    for (headers <- request.headers)
      req = req <:< headers

    for (requestBody <- request.requestBody)
      req = req << requestBody

    request.method match {
      case "GET"    => req.GET
      case "POST"   => req.POST
      case "PUT"    => req.PUT
      case "DELETE" => req.DELETE
      case _        => req.GET
    }
  }
}
