package scala.crawlgrowlcow.crawling

import scalaj.http.HttpRequest

/**
  * This class used to represent crawl task:
  * Each task is an object which contains result
  */
abstract class CrawlWork(domain: String) {
  var hasAuth = false
  val domains: List[String] = List()
  def work(fetched: CrawlResponse): CrawlResult // this method should be defined in child class

  def authorize(client: HttpRequest) = {
    hasAuth = true
  }

  def setDomains(domains:List[String]) {
    this.domains ++ domains
  }
}

