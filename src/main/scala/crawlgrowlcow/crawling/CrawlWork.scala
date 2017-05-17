package scala.crawlgrowlcow.crawling

import scala.collection.mutable.ListBuffer
import scalaj.http.HttpRequest

/**
  * This class used to represent crawl task:
  * Each task is an object which contains result
  */
abstract class CrawlWork {
  var hasAuth = false
  var domains: List[String] = null
  var pathExceptions: List[String] = null
  def work(fetched: CrawlResponse): CrawlResult // this method should be defined in child class

  def setParams(client: HttpRequest) = {}

  def setDomains(domains:List[String]) {
    this.domains = domains
  }

  def setPathExceptions(exceptions: List[String]): Unit = {
    this.pathExceptions = exceptions
  }


  def init()

  def getNewInstace(): CrawlWork
}

