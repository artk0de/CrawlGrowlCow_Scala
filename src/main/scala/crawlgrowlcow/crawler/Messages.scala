package crawlgrowlcow.crawler

import java.net.URL
/**
  * Created by krikun on 30.11.16.
  */

case class Start()
case class Scrap(url: URL)
case class Index(url: URL, content: Content)
case class Content(title: String, meta: String, urls: List[URL])
case class ScrapFinished(url: URL)
case class IndexFinished(url: URL, urls: List[URL])
case class ScrapFailure(url: URL, reason: Throwable)
