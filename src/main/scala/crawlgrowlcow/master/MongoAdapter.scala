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
object MongoAdapter {
  var dbName: String = null
  var connection: String = null

  var client: MongoClient = null
  var db: MongoDB = null

  var tasks: MongoCollection = null
  var results: MongoCollection = null

  def initDB(dbName: String, connString: String) = {
    client = MongoClient(MongoClientURI(connString))
    db = client(dbName)

    tasks = db("tasks")
    results = db("results")
  }


  def pushTasks(urls: List[String], prev_url: String) = {
    for (url <- urls) {
      val result = tasks.find(MongoDBObject("url" -> MongoDBObject("$in" -> urls)))
      if (result.size < 1) {
        val query = MongoDBObject("url" -> url,
                                  "prevUrl" -> prev_url,
                                  "isCompleted" -> false,
                                  "isError" -> false,
                                  "isLoaded" -> false)
        tasks.insert(query)
      }
    }
  }

  def pushTask(url: String, prev_url: String) = {
    val result = tasks.find(MongoDBObject("url" -> url))
    println(result.size)
    if (result.size < 1) {
      val query = MongoDBObject("url" -> url,
                                "prevUrl" -> prev_url,
                                "isCompleted" -> false,
                                "isError" -> false,
                                "isLoaded" -> false)
      tasks.insert(query)
    }
  }

  def pullTasks(n: Int): List[String] = {
    var result: ListBuffer[String] = ListBuffer()
    val allDocs = tasks.find(MongoDBObject("isLoaded" -> false, "isCompleted" -> false)).limit(n)
    if (allDocs.size > 0) {
      val updateTo: ListBuffer[ObjectId] = ListBuffer()

      for (doc <- allDocs) {
        updateTo += doc._id.get
        result += doc.get("url").toString
      }
      tasks.update(MongoDBObject("_id" -> MongoDBObject("$in" -> updateTo.toList)),
                   MongoDBObject("$set" -> MongoDBObject("isLoaded" -> true)),
                   multi = false)
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
    val res = results.find(MongoDBObject("url" -> url))
    if (res.size < 1) {
      results.insert(MongoDBObject("url" -> url,
                                   "result" -> result))
    }
  }
}
