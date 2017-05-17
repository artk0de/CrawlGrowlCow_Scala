package crawlgrowlcow.crawling

import scala.crawlgrowlcow.crawling.CrawlResult

/**
  * Created by art2rik1 on 01.12.16.
  */
trait CrawlObserver {
  def update(url:String, urls: List[String], result: CrawlResult)
}

trait CrawlObservable {

  var observer: CrawlObserver = null
  def registerObserver(observer: CrawlObserver): Unit = {
    this.observer = observer
  }
  def notifyResult(url: String, urls: List[String], result: CrawlResult) = {
    observer.update(url, urls, result)
  }
}
