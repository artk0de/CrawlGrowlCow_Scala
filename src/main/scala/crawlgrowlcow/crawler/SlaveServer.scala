package crawlgrowlcow.crawler

import akka.actor.{ActorSystem, PoisonPill, Props}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import java.net.URL

import akka.util.Timeout
/**
  * Created by krikun on 30.11.16.
  */
object SlaveServer {
  val address = new URL("http://localhost:9000/getJob")
  implicit val system = ActorSystem("crawlers")

  val supervisor = system.actorOf(Props(new Supervisor(system, address)))

  println("start")
  supervisor ! Start

//  implicit val timeout = Timeout(10.seconds)
//  system.awaitTermination()
//  supervisor ! PoisonPill
//  system.shutdown()
}
