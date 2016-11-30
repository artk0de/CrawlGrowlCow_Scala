package crawlgrowlcow.distributing

import org.json4s.JsonAST.{JObject, JArray}

import scala.crawlgrowlcow.crawling.CrawlResult
import scalaj.http._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.write
/**
  * Created by art2rik1 on 30.11.16.
  */
case class CrawlGrowlCowApi(ip: String, port: String) {

  def getDaemonIds(n: Int): List[Int] = {
    // get IDs
    return List(1,2,3,4)
  }

  def getTasks(n: Int): List[String] = {
    val url = "http://$ip:$port/getTasks"
    val resp: HttpResponse[String] = Http(url).method("GET")
                                              .param("n", n.toString)
                                              .asString
    val jsonTasks = parse(resp.body)

    return jsonTasks.extract[List[String]]
  }

  def pullResult(url: String, result: CrawlResult, urls: List[String])  = {
    val url = "http://$ip:$port/pullResult"

    val urls_json = write(urls)

    Http(url).method("POST").param("url", url)
      .param("result", result.toJsonString)
      .param("urls", compact(render(urls_json))).asBytes
  }

  def pul



}
