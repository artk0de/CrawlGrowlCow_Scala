import org.apache.log4j.BasicConfigurator
import scalaj.http._

/**
  * Created by art2rik1 on 21.11.16.
  */
object Tests {
  def main(args: Array[String]) {
    BasicConfigurator.configure()
    val url = "https://ru.wikipedia.org/wiki/Луций_Корнелий_Цинна"


    val request: HttpRequest = Http(url)

    val response: HttpResponse[String] = request.asString


    val cookies = response.cookies
    val headers = response.headers
    print(response.cookies)
  }
}
