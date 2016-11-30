package crawlgrowlcow.crawler

/**
  * Created by krikun on 30.11.16.
  */

import java.net.URL
import akka.actor.{Actor, ActorSystem, Props, _}
import org.jsoup.Jsoup
import scala.language.postfixOps

class Supervisor(system: ActorSystem, address: URL) extends Actor {
  val indexer = context actorOf Props(new Indexer(self))

  val maxPages = 100
  val maxRetries = 2

  var numVisited = 0
  var toScrap = Set.empty[URL]
  var scrapCounts = Map.empty[URL, Int]
  var host2Actor = Map.empty[String, ActorRef]

  def getJob = {
    val tasks = scala.io.Source.fromURL(address).getLines
    for (url <- tasks) toScrap += new URL(url)
  }

  def receive: Receive = {
    case Start =>
      getJob
      scrap(toScrap.toList.head)
    case ScrapFinished(url) =>
      println(s"scraping finished $url")
    case IndexFinished(url, urls) =>
      println(urls)
      if (numVisited < maxPages)
        urls.toSet.filter(l => !scrapCounts.contains(l)).foreach(scrap)
      checkAndShutdown(url)
    case ScrapFailure(url, reason) =>
      val retries: Int = scrapCounts(url)
      println(s"scraping failed $url, $retries, reason = $reason")
      if (retries < maxRetries) {
        countVisits(url)
        host2Actor(url.getHost) ! Scrap(url)
      } else
        checkAndShutdown(url)
  }

  def checkAndShutdown(url: URL): Unit = {
    toScrap -= url
    println(s"check ${url}")
    // if nothing to visit
    if (toScrap.isEmpty) {
      println("queue is empty")
      self ! PoisonPill
      system.shutdown()
    } else {
      scrap(toScrap.toList.head)
    }
  }

  def scrap(url: URL) = {
    val host = url.getHost
    println(s"host = $host")
    if (!host.isEmpty) {
      val actor = host2Actor.getOrElse(host, {
        val buff = system.actorOf(Props(new Crawler(self, indexer)))
        host2Actor += (host -> buff)
        buff
      })

      numVisited += 1
      countVisits(url)
      actor ! Scrap(url)
    }
  }

  def countVisits(url: URL): Unit = scrapCounts += (url -> (scrapCounts.getOrElse(url, 0) + 1))
}
