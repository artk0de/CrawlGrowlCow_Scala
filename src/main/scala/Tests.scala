import org.apache.log4j.BasicConfigurator
import org.json4s.DefaultFormats
import scalaj.http._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import com.mongodb.casbah.Imports._


/**
  * Created by art2rik1 on 21.11.16.
  */
object Tests {
  def main(args: Array[String]) {
    implicit val formats = DefaultFormats
    val json = parse(
      """["xyu", "zhopa", "pizada"] """)

    println(json.extract[List[String]])
  }
}
