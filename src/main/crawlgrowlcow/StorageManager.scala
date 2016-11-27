package crawlgrowlcow

import org.mongodb.scala._

/**
  * Created by art2rik1 on 21.11.16.
  */
class StorageManager(connectionString: String, dbname: String) {
  val mongoClient: MongoClient = MongoClient(connectionString)
  val database: MongoDatabase = mongoClient.getDatabase(dbname)
  val resultCollection: MongoCollection[Document] = database.getCollection("results");
  val crawlCollection: MongoCollection[Document] = database.getCollection("crawl");

  

}
