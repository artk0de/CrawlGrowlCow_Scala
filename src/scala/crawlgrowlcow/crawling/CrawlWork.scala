package scala.crawlgrowlcow.crawling

/**
  * This class used to represent crawl task:
  * Each task is an object which contains result
  */
abstract class CrawlWork(domain: String) {
  val domains: List[String] = List()
  def work(fetched: CrawlResponse): CrawlResult // this method should be defined in child class
  def authorize()
  def setDomains(domains:List[String]) {
    this.domains ++ domains
  }
}

