package crawlgrowlcow.master.server

import akka.actor.Actor
import org.json4s.JsonAST.JArray
import spray.routing._
import spray.http._
import spray._
import MediaTypes._
import crawlgrowlcow.master._
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.{write, read}
import org.json4s.native.Serialization


/**
  * Created by krikun on 30.11.16.
  */

case class TaskServerActor() extends Actor with TaskServer {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(taskRoute)
}

// this trait defines our service behavior independently from the service actor
trait TaskServer extends HttpService {
  // should be change to list from db <-> to db...
//  implicit val formats = DefaultFormats
  implicit val formats = Serialization.formats(NoTypeHints)
  val taskRoute =
    (path("getTasks") & post) {
      parameter("n") { number => complete {
        val tasks = MongoAdapter.pullTasks(number.toInt)
        val json_res = ("result" -> tasks)
        compact(render(json_res))
      }
      }
    } ~
    (path("pushResult") & post) {
      parameters("url", "urls"?, "result") { (url_g, urls, result) => respondWithStatus(200) {
        complete {
          MongoAdapter.updateTaskStatus(url_g, isCompleted = true)

          var urls_list: List[String] = null
          if (urls.nonEmpty) {
            val json =parse(urls.get)
            urls_list = (json \ "urls").extract[List[String]]
          } else {
            urls_list = List()
          }

          MongoAdapter.pushTasks(urls_list, url_g)
          MongoAdapter.pushResults(url_g, result)
          ""
        }
      }
      }
    }
}

