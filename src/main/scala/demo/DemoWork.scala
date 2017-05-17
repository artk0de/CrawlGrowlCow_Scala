package scala.demo

import scala.crawlgrowlcow.crawling._
import scala.crawlgrowlcow._
import scala.crawlgrowlcow.main.CrawlGrowlCow
import scalaj.http.HttpRequest

/**
  * Created by art2rik1 on 28.11.16.
  */

object App {
  def main(args: Array[String]) {
    CrawlGrowlCow.start(new DemoWork(), args)
  }
}


class DemoWork() extends CrawlWork {

  override def work(response: CrawlResponse): CrawlResult = {
    println(response.toJsonString)
    return CrawlResult(null)
  }

  override def setParams(client: HttpRequest) = {
    client.method("GET")
  }


  override def init() = {}

  override def getNewInstace(): CrawlWork = {
    return new DemoWork
  }
}
