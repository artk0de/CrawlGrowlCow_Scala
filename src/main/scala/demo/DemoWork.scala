package scala.demo

import scala.crawlgrowlcow.crawling.{CrawlResponse, CrawlResult, CrawlWork}
import scalaj.http.HttpRequest

/**
  * Created by art2rik1 on 28.11.16.
  */

object DemoWork extends CrawlWork {

  override def work(fetched: CrawlResponse): CrawlResult = {
    println(fetched.toJson.toString)
    CrawlResult(fetched.toJson)
  }

  // this method should be defined in child class
  override def authorize(client: HttpRequest): Unit = super.authorize(client)
}
