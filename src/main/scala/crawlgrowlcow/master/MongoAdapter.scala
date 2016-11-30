package crawlgrowlcow.master

import org.json4s.JValue
import org.json4s.JsonAST.JValue
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import com.mongodb.casbah.Imports._

import scala.collection.mutable.ListBuffer

/**
  * Created by art2rik1 on 29.11.16.
  */
case class MongoAdapter(connection:String,
                        dbName: String) {
  val client = MongoClient(MongoClientURI(connection))
  val db = client(dbName)

  val tasks = db("tasks")
  val results = db("results")

  def pushTasks(urls: List[String], prev_url: String) = {

    for (url <- urls) {
      val result = tasks.find(MongoDBObject("url" -> url))
      if (result.nonEmpty) {
        val query = MongoDBObject("url" -> url,
                               "prevUrl" -> prev_url,
                               "isCompleted" -> false,
                               "isError" -> false,
                               "isLoaded" -> false)
        tasks.insert(query)
      }
    }
  }

  def pullTasks(n: Int): List[String] = {
    var result: ListBuffer[String] = ListBuffer()
    val allDocs = tasks.find(MongoDBObject("isLoaded" -> false, "isCompleted" -> false )).limit(n)
    if (allDocs.nonEmpty) {
      val updateTo: ListBuffer[ObjectId] = ListBuffer()

      for (doc <- allDocs) {
        updateTo += doc._id.get
        result += doc.get("url").toString
      }
      tasks.update(MongoDBObject("_id" -> MongoDBList(updateTo.toList)),
                   MongoDBObject("isLoaded" -> true))
    } else {
      val allDocs = tasks.find(MongoDBObject("isLoaded" -> false, "isCompleted" -> false )).limit(n)
      for (doc <- allDocs) {
        result += doc.get("url").toString
      }
    }

    return result.toList
  }

  def updateTaskStatus(taskUrl: String,
                       isCompleted: Boolean = true,
                       isError: Boolean = false) = {
    tasks.update(MongoDBObject("url" -> taskUrl),
                 MongoDBObject("isCompleted" -> isCompleted, "isError" -> isError))
  }


  def pushResults(url: String, result: String) = {
    results.insert(MongoDBObject("url" -> url,
                                 "result" -> MongoDBObject(result)))
  }
}
