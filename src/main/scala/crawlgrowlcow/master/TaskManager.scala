package crawlgrowlcow.master

import java.io.File

/**
  * Created by art2rik1 on 29.11.16.
  */
object TaskManager {
  var jobs = List[String]()

  def initURLsFromFile(filename: String): Unit = {
    val f = new File(filename)
    if (f.exists) {
      jobs = scala.io.Source.fromFile(filename).getLines.toList
    }
  }

  def getJob: String = {
    jobs.take(10).fold("")((res, url) => res.concat(url + "\n"))
  }

  def addJob(url: String) = {
    jobs ++ url
  }
}
