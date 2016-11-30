package scala.crawlgrowlcow.main

import java.net.InetAddress

import crawlgrowlcow.crawler.SlaveServer
import crawlgrowlcow.distributing.DaemonHandler
import crawlgrowlcow.master.TaskManager
import crawlgrowlcow.master.server.MasterServer

import scala.crawlgrowlcow._
import scala.crawlgrowlcow.crawling.CrawlWork
import scala.demo.DemoWork
/**
  * This class will be used as main point of program like executable file
  */
object CrawlGrowlCow {
  var address = ""
  var daemons = 0
  var urls = ""
  var port = 0

  val usage =
    """
      Usage: executable_name [--master port:ip] [--daemons num] [--urls filename]
    """

  def parseArgs(args: Array[String]) = {
    println(args.toList.toString)
    if (args.length == 0) println(usage)
    val arglist = args.toList

    type OptionMap = Map[Symbol, Any]

    def nextOptionNode(map: OptionMap, list: List[String]): OptionMap =
    {
      def isSwitch(s: String) = s(0) == '-'
      list match {
        case Nil => map
        case "--master" :: string :: tail => nextOptionNode(map ++ Map('address -> string), tail)
        case "--daemons" :: value :: tail => nextOptionNode(map ++ Map('daemons -> value.toInt), tail)
        case "--urls" :: string :: tail => nextOptionNode(map ++ Map('urls -> string), tail)
//        case string :: opt2 :: tail if isSwitch(opt2) => nextOptionNode(map ++ Map('infile -> string), list.tail)
//        case string :: Nil => nextOptionNode(map ++ Map('infile -> string), list.tail)
        case option :: tail =>
          println("Unknown option " + option)
          println(usage)
          sys.exit(1)
      }
    }

    val optionsNode = nextOptionNode(Map(), arglist)
    println(optionsNode)

    for (option <- optionsNode) {
      option._1 match {
        case 'address => this.address = option._2.asInstanceOf[String]
        case 'daemons => this.daemons = option._2.asInstanceOf[Int]
        case 'urls => this.urls = option._2.asInstanceOf[String]
      }
    }
//    println(address + " " + port + " " + daemons + " " + urls)
  }

  def apply(worker: CrawlWork, args: Array[String]) = {
    parseArgs(args)
    if (this.address == "") {
      startMaster
    } else {
      startNode(worker, address)
    }
  }

  private def startMaster = {
    println("master")
//    val daemon = DaemonHandler(1, worker)
    // startup task server
    TaskManager.initURLsFromFile(urls)
    MasterServer
  }

  private def startNode(worker: CrawlWork, serverAddress: String)= {
    println("node")
    SlaveServer
  }

}

