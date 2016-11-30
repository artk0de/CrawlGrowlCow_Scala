package scala.crawlgrowlcow.crawling
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

/**
  * Created by art2rik1 on 28.11.16.
  */
case class CrawlResult(results: JValue) {
  def toJsonString = compact(render(results))
}

