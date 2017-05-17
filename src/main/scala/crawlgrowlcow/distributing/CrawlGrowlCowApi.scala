package crawlgrowlcow.distributing

import org.json4s.DefaultFormats
import org.json4s.JsonAST.{JObject, JArray}

import scala.crawlgrowlcow.crawling.CrawlResult
import scalaj.http._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.write
/**
  * Created by art2rik1 on 30.11.16.
  */
object CrawlGrowlCowApi {
  implicit val formats = DefaultFormats
  var ip: String = null
  var port: Int = 80

  def setServer(ip: String, port: Int) = {
    this.ip = ip
    this.port = port
  }

  def getTasks(n: Int): List[String] = {
      val url = "http://" + ip + ":" + port.toString + "/getTasks"
      println(url)
      val resp: HttpResponse[String] = Http(url).method("POST")
                                       .param("n", n.toString)
                                       .asString
      val jsonTasks = parse(resp.body)

      return (jsonTasks \ "result").extract[List[String]]
  }

  def pushResult(url_res: String, result: CrawlResult, urls: List[String])  = {
    val url = "http://" + ip + ":" + port.toString + "/pushResult"

    val urls_json = ("urls" -> urls)
    println(compact(render(urls_json)))
    Http(url).method("POST").params(Map(("url" -> url_res),
    ("result"-> result.toJsonString),
    ("urls" -> compact(render(urls_json))))).asString
  }
}
