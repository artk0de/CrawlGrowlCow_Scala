package crawlgrowlcow

import scala.collection.mutable.Queue
import scala.collection.mutable.Stack

/**
  * Created by art2rik1 on 21.11.16.
  */
class CrawlDaemon(worker: CrawlWork) {
  private val crawlTasks = Queue[String]()
  private val crawlResults = Stack[String]()
  private var isEnabled = false


  private def process() {
    // TODO: add multiprocessing
    while(isEnabled) {
      if (crawlTasks.nonEmpty) {
        val url = crawlTasks.dequeue()
        val result = fetch(url)
        val fetched_urls = result._1
        val fetched_result = result._2
//        val processed_data =  worker.work(fetched_result)
//        crawlResults.push(processed_data)
      } else {
        Thread.sleep(5000)
      }
    }
  }

  private def fetch(url: String): (List[String], List[String]) = {
    //connect to a server
    //get data
    val fetched_urls: List[String] = List()
    val json_data: List[String] = List()
    // process raw data to JSON format
    return (fetched_urls, json_data)
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
