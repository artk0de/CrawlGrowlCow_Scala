package crawlgrowlcow.master

import org.mongodb.scala._
/**
  * Created by art2rik1 on 29.11.16.
  */
object MongoAdapter {
  val db = MongoClient("")
  val mongoClient: MongoClient = MongoClient("mongodb://localhost")
  val database: MongoDatabase = mongoClient.getDatabase("mydb")
}
