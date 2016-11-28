package scala.crawlgrowlcow.crawling

import scala.collection.mutable.{Queue, Stack}
import scalaj.http._


class CrawlDaemon(worker: CrawlWork) {
  private val crawlTasks = Queue[String]()
  private val crawlResults = Stack[String]()
  private var isEnabled = false
  private var attempts = 20



  private def process() {
    while(isEnabled) {
      if (crawlTasks.nonEmpty) {
        val url = crawlTasks.dequeue()
//        try {

          val response = getResponse(url)
          val urls = fetchUrls(response.body)
//        }
//        val processed_data =  worker.work(fetched_result)
//        crawlResults.push(processed_data)
      } else {
        Thread.sleep(5000)
      }
    }
  }

  private def fetchUrls(body: String): List[String]= {
    val urls: List[String] = List()
    for (domain <- worker.domains) {
      val pattern = "(http(s?)://)?*$domain/*(/s|<|\")".r

    }
    return urls
  }

  private def getResponse(url: String): HttpResponse[String] = {
    return Http(url).asString
  }

  def addTask(url: String) {
    crawlTasks += url
  }

  def getLastResult(): String = {
    return crawlResults.pop()
  }

  def enable() { // Activates a daemon
    isEnabled = true
    process()
  }

  def disable() { // Disables a daemon
    isEnabled = false
  }
}
