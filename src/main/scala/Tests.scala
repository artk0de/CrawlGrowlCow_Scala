import org.apache.log4j.BasicConfigurator
import scalaj.http._
import sys.process._
/**
  * Created by art2rik1 on 21.11.16.
  */
object Tests {
  def main(args: Array[String]) {
//    BasicConfigurator.configure()
//    val url = "https://ru.wikipedia.org/wiki/Луций_Корнелий_Цинна"
//
//    val head: Map[String, String] = Map()
//    val parms: Map[String, String] = Map()
//
//    val meth = "GET"
//    val request: HttpRequest = Http(url).headers(head).method(meth).params(parms)
//
//    val response: HttpResponse[String] = request.asString
//
//
//    val cookies = response.cookies
//    val headers = response.headers
//
//    print(response.cookies)
      print("XYU")
    val res = "mongod".!!
    println(res)
  }
}
