package crawlgrowlcow.distributing


import crawlgrowlcow.crawling.CrawlObserver

import scala.collection.immutable.SortedSet
import scala.collection.mutable
import scala.collection.mutable.{ListBuffer, Queue}
import scala.concurrent.duration
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.crawlgrowlcow.crawling.{CrawlResult, CrawlRequest, CrawlDaemon, CrawlWork}
import scala.demo.DemoWork
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent._
import ExecutionContext.Implicits.global

/**
  * Created by art2rik1 on 29.11.16.
  */
case class DaemonHandler(daemons_count: Int,
                         worker: CrawlWork) extends CrawlObserver{

  var daemons: List[CrawlDaemon] = initDaemons()
  val taskNumber = 20

  def initDaemons():List[CrawlDaemon] = {
    var buffer: ListBuffer[CrawlDaemon] = ListBuffer()
    for (i <- 1 to daemons_count) {
      val work: CrawlWork= worker.getNewInstace()
      val daemon = CrawlDaemon(work, i)
      daemon.registerObserver(this)
      buffer += daemon
    }
    println(buffer)
    return buffer.toList
  }

  def startWork() = {
    initDaemonThreads()
    initMainThread()
  }

  private def initDaemonThreads() = {
    for (daemon <- daemons) {

      new Thread(new Runnable {
        override def run() = daemon.enable()
      }).start()
    }
  }

  private def initMainThread() = {
    new Thread(new Runnable {
      override def run() = {
        while (true) {

          val f_tasks: Future[mutable.Queue[String]] = Future {
            Thread.sleep(10000)
            Queue(CrawlGrowlCowApi.getTasks(taskNumber): _*)
          }

          Await.ready(f_tasks, 60 second)

          f_tasks.onSuccess {
            case tasks => {
              var i = 0
              while (tasks.nonEmpty) {
                val task = tasks.dequeue()
                if (task != null) {
                  i %= daemons_count
                  daemons(i).addTask(new CrawlRequest(task))
                  i += 1
                }
              }
            }
          }
        }
      }
    }).start()
  }

  override def update(url: String, urls: List[String], result: CrawlResult): Unit = {
    urls.grouped(15).toList foreach  { urls_list =>
      CrawlGrowlCowApi.pushResult(url, result, urls_list)
    }
  }
}
