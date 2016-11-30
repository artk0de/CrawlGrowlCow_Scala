package scala.crawlgrowlcow.crawling

import scala.collection.mutable.{Queue, Stack}
import scalaj.http._


case class CrawlDaemon(worker: CrawlWork, id: Int) extends Comparable[CrawlDaemon] {
  private val crawlTasks = Queue[CrawlRequest]()
  private val crawlResults = Stack[(CrawlResult, CrawlResponse)]()
  private var isEnabled = false
  private var attempts = 20

  private def process() {
    while (isEnabled) {
      if (crawlTasks.nonEmpty) {
        val req = crawlTasks.dequeue() // get a task

        if (worker.hasAuth) {
          worker.authorize(req.client)
        }
        val response: HttpResponse[String] = req.execute()

        if (response.isSuccess) {
          val urls = fetchUrls(response.body)

          var status: Int = 200

          if (response.header("Status").isDefined) {
            status = response.header("Status").get.toInt
          }

          val crawlResp = CrawlResponse(req, status, response.headers, response.body) // create response object
          val result: CrawlResult = worker.work(crawlResp)
          if (result.results == null) {
            crawlResults.push((result, crawlResp)) // do usefull job
          } else {
            crawlResults.push((null, crawlResp))
          }

        } else { // if connection wasn't successfully

        }
      } else {
        // Make a pause to wait a new task
        Thread.sleep(1000)
      }
    }
  }

  private def fetchUrls(body: String): List[String] = {
    //TODO: Fetch urls
    val urls: List[String] = List()
    for (domain <- worker.domains) {
      val pattern = "(http(s?)://)?*$domain/*(/s|<|\")".r
      //<a\s+(?:[^>]*?\s+)?href="([^"]*(wiki[^"]*))"

    }
    return urls
  }

  def addTask(url: CrawlRequest) {
    crawlTasks += url
  }

  def getLastResult(): (CrawlResult, CrawlResponse) = {
    return crawlResults.pop()
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
