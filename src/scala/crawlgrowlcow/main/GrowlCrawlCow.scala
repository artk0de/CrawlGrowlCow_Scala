package scala.crawlgrowlcow.main

import  scala.crawlgrowlcow._
import scala.crawlgrowlcow.crawling.CrawlWork
/**
  * This class will be used as main point of program like executable file
  */
object GrowlCrawlCow {

  val master = "master"
  val node = "node"

  def start(worker: CrawlWork, args: Array[String]) = {
    args(0) match {
      case master => startMaster(worker, args)
      case node => startNode(worker, args)
      case _ => throw new IllegalArgumentException("First arg must be $master or $node!")
    }
  }
  private def startMaster(worker: CrawlWork, args: Array[String])= {

  }

  private def startNode(worker: CrawlWork, args: Array[String])= {

  }
}
