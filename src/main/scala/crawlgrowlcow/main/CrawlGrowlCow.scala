package scala.crawlgrowlcow.main

import java.io.File
import java.net.InetAddress

import crawlgrowlcow.distributing.{CrawlGrowlCowApi, DaemonHandler}
import crawlgrowlcow.master.{MongoAdapter, MasterServer}
import org.apache.log4j.BasicConfigurator

import scala.collection.mutable.ListBuffer
import scala.crawlgrowlcow._
import scala.crawlgrowlcow.crawling.CrawlWork


/**
  * This class will be used as main point of program like executable file
  */
object CrawlGrowlCow {
  var daemons = 0
  var urls = ""
  var port = 0
  var masterUrl: String = "localhost"
  var isServer = false
  var dbName: String = null
  var mongoString: String = null

  val usage =
    """
      Usage: executable_name [--master port:ip] [--daemons num] [--urls filename]
    """

  private def parseArgs(args: Array[String]) = {

    if (args.length == 0) println(usage)
    val arglist:ListBuffer[String] = ListBuffer(args.toList: _*)


    type OptionMap = Map[Symbol, Any]
    arglist(0) match {
      case "master" => this.isServer = true
      case "slave" => this.isServer = false
      case _ => {
        println("""Type of run must be "master" or "slave"! """)
        sys.exit(1)
      }
    }
    arglist.remove(0)
    def nextOptionNode(map: OptionMap, list: List[String]): OptionMap = {
      def isSwitch(s: String) = s(0) == '-'
      list match {
        case Nil => map
        case "--daemons" :: value :: tail => nextOptionNode(map ++ Map('daemons -> value.toInt), tail)
        case "--urls" :: string :: tail => nextOptionNode(map ++ Map('urls -> string), tail)
        case "--host" :: string :: tail => nextOptionNode(map ++ Map('server -> string), tail)
        case "--port" :: value :: tail => nextOptionNode(map ++ Map('port -> value.toInt), tail)
        case "--dbname" :: string :: tail => nextOptionNode(map ++ Map('dbname -> string), tail)
        case "--db" :: string :: tail => nextOptionNode(map ++ Map('db -> string), tail)
        case "master" :: string :: tail => nextOptionNode(map, tail)
        case "slave" :: string :: tail =>nextOptionNode(map, tail)
        case string :: opt2 :: tail if isSwitch(opt2) => nextOptionNode(map ++ Map('infile -> string), list.tail)
        case string :: Nil => nextOptionNode(map ++ Map('infile -> string), list.tail)
        case option :: tail =>
        println("Unknown option " + option)
        println(usage)
        sys.exit(1)
      }
    }

    val optionsNode = nextOptionNode(Map(), arglist.toList)
    println(optionsNode)

    for (option <- optionsNode) {
      option._1 match {
        case 'daemons => this.daemons = option._2.asInstanceOf[Int]
        case 'urls => this.urls = option._2.asInstanceOf[String]
        case 'server => this.masterUrl = option._2.asInstanceOf[String]
        case 'port => this.port = option._2.asInstanceOf[Int]
        case 'dbname => this.dbName = option._2.asInstanceOf[String]
        case 'db => this.mongoString = option._2.asInstanceOf[String]
      }
    }
    //    println(address + " " + port + " " + daemons + " " + urls)
  }

  def start(worker: CrawlWork, args: Array[String]) = {
    parseArgs(args)
    BasicConfigurator.configure()
    if (this.isServer) {
      startMaster(worker)
    } else {
      startSlave(worker)
    }
  }

  private def startMaster(worker: CrawlWork) = {

    CrawlGrowlCowApi.setServer(masterUrl, port)
    MongoAdapter.initDB(dbName, mongoString)
    if (urls.nonEmpty) {
      initURLsFromFile(urls)
    }
    MasterServer.run(port)

    if (daemons > 0) {
      val handler = DaemonHandler(daemons, worker)
      handler.startWork()
    }


  }

  private def initURLsFromFile(filename: String): Unit = {
    val f = new File(filename)
    var tasks: Set[String] = null
    if (f.exists) {
      tasks = scala.io.Source.fromFile(filename).getLines.toSet
    } else {
      throw new IllegalArgumentException("There no such files $filename")
    }
    if (tasks.isEmpty) {
      throw new IllegalArgumentException("A file must contain at least 1 link!")
    }
    for (task <- tasks) {
      MongoAdapter.pushTask(task, null)
    }
  }

  private def startSlave(worker: CrawlWork) = {
    CrawlGrowlCowApi.setServer(masterUrl, port)
    if (daemons > 0) {
      val handler = DaemonHandler(daemons, worker)
      handler.startWork()
    } else {
      throw new IllegalArgumentException("Daemons count must be >= 1!")
    }
  }

}

