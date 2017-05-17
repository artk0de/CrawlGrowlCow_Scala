# CrawlGrowlCow
CrawlGrowlCow is the framework for distributing crawling or parsing based on Scala. Its allow user to crawl on different machines and uses MongoDB to collect data.


# User's guide
## Setup & launch

1. Install Scala
2. Install MongoDB
3. Run MongoDB
4. Implement CrawlWork class
5. Compile & run implemented class as master or slave node.

## Create a worker

A *CrawlWork* class represents an action which executes for every single link.

```scala
package scala.demo

import scala.crawlgrowlcow.crawling.{CrawlResponse, CrawlResult, CrawlWork}
import scalaj.http.HttpRequest

object Demo extends App { // Starting object
  val crawl = CrawlGrowlCow(DemoWork, args)  // Start CrawlGrowlCow node 
 }                                          // by using your implemented class

object DemoWork extends CrawlWork { // Worker demo class

  override def work(fetched: CrawlResponse): CrawlResult = {
    println(fetched.toJson.toString) // prints content of a page
    CrawlResult(fetched.toJson) //
  }

  // this method should be defined in child class
  override def authorize(client: HttpRequest): Unit = super.authorize(client)
}
```
## Run master

## Run a slave

## How this works

![alt text](https://github.com/art2rik/CrawlGrowlCow/blob/master/img/structure.png "System structure")
![alt text](https://github.com/art2rik/CrawlGrowlCow/blob/master/img/workflow.png "Workflow")

![alt text](https://github.com/adam-p/markdown-here/raw/master/src/common/images/icon48.png "Logo Title Text 1")

