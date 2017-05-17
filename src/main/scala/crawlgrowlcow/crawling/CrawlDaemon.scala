package scala.crawlgrowlcow.crawling


import java.net.URI

import crawlgrowlcow.crawling.CrawlObservable

import scala.collection.mutable.{ListBuffer, Queue, Stack}
import scala.concurrent.{Await, Future}
import scalaj.http._
import scala.concurrent.duration._
import scala.concurrent._
import ExecutionContext.Implicits.global


case class CrawlDaemon(worker: CrawlWork, id: Int) extends Comparable[CrawlDaemon] with CrawlObservable {
  val crawlTasks: Queue[CrawlRequest] = Queue[CrawlRequest]()
  private var isEnabled = false
  private val maxAttempts = 20
  private var attempts = maxAttempts
  private var lastTask: CrawlRequest = null

  private def process() {
    while (isEnabled) {
      var req: CrawlRequest = null
      if (crawlTasks.size >= 1) {
        req = crawlTasks.dequeue()

        worker.setParams(req.client)

        try {
          val response: HttpResponse[String] = req.execute()

          if (response.isSuccess) {
            val urls = fetchUrls(response.body, req.url)

            var status: Int = 200

            if (response.header("Status").isDefined) {
              status = response.header("Status").get.split(' ')(1).toInt
            }

            val crawlResp = new CrawlResponse(req, status, response.headers, response.body) // create response object
            val result: CrawlResult = worker.work(crawlResp)

            observer.update(req.url, urls, result) // do usefull job
          }
        }
        catch {
          case e: Exception => observer.update(req.url, List(), CrawlResult(null)) // do usefull job
        }
      } else {
        val kostyl: Future[String] = Future {
          Thread.sleep(1000)
          ""
        }
        Await.ready(kostyl, 30 seconds)
      }
    }
  }

  private def fetchUrls(body: String, url: String): List[String] = {
    // find inner urls
    val domain: String = new URI(url).getHost

    val urls: ListBuffer[String] = ListBuffer()
    val pattern = "<a\\s+(?:[^>]*?\\s+)?href=\"/([^\".]+([^\".]*/[^\".]*))\"".r("link", "link2")
    (pattern findAllMatchIn (body)) foreach {
      m => {
        val link = m.group("link")
        urls += ("https://" + domain + "/" + link)
      }
    }
    return urls.toList
  }

  def addTask(url: CrawlRequest) {
    crawlTasks.enqueue(url)
  }

  def enable() {
    // Activates a daemon
    isEnabled = true
    process()
  }

  def disable() {
    // Disables a daemon
    isEnabled = false
  }

  override def compareTo(that: CrawlDaemon): Int = this.crawlTasks.size - that.crawlTasks.size
}
