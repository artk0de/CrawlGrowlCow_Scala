package crawlgrowlcow.distributing

import scala.collection.immutable.HashMap
import scala.crawlgrowlcow.crawling.{CrawlDaemon, CrawlWork}

/**
  * Created by art2rik1 on 29.11.16.
  */
case class DaemonHandler(n: Int,
                         worker: CrawlWork) {

  var daemons: HashMap[Int, CrawlDaemon] = HashMap()

  def initDaemons(worker: CrawlWork) = {
    val ids = CrawlGrowlCowApi.getDaemonIds(n)
    for (id <- ids) {
      daemons += (id -> CrawlDaemon(worker, id))
    }
  }

  def startWork() = {
    initDaemonThreads()
    initMainThread()
  }

  private def initDaemonThreads() = {
    for ((_, daemon) <- daemons) {

      // Run new thread with a daemon
      new Thread(new Runnable {
        override def run() = daemon.enable()
      }).start()
    }
  }

  private def initMainThread() = {
    new Thread( new Runnable {
      override def run() = {
        //getTasks
      }
    }).start()
  }

}
