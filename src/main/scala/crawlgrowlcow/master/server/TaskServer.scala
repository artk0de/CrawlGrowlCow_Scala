package crawlgrowlcow.master.server

import akka.actor.Actor
import spray.routing._
import spray.http._
import spray.json._
import DefaultJsonProtocol._
import MediaTypes._
import crawlgrowlcow.master.TaskManager

/**
  * Created by krikun on 30.11.16.
  */

class TaskServerActor extends Actor with TaskServer {

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
  val taskRoute =
    path ("getJob") {
        get {
          respondWithStatus(200) {
            complete {
              TaskManager.getJob
            }
          }
        }
      }
}
