# CrawlGrowlCow
CrawlGrowlCow is the framework for distributing crawling or parsing based on Scala. Its allow user to crawl on different machines and uses MongoDB to collect data.

# Features

* Flexible distributed parsing and crawling
* Simple
* Fast
* Multithreading
* New nodes adds easily
* Supports OAuth and other authorization types

# User's guide
## Setup & launch

1. Install Scala
2. Install MongoDB
3. Run MongoDB
4. Create worker
5. Run worker as a node or master

## Little theory

The *CrawlGrowlCow* intended for crawling, parsing and extraction of different data from any web-resourses. 

### Structure
The system consists of:
* Master server (or central node)
* Slave node
* Database, assigned to master

**Master** acts as central cerver and interacting with database. It is receives results from slave nodes and recording it to local database.

**Node** 
![alt text](https://github.com/art2rik/CrawlGrowlCow/blob/master/img/structure.png "System structure")

### Workflow
![alt text](https://github.com/art2rik/CrawlGrowlCow/blob/master/img/workflow.png "Workflow")

## Create a worker

A *CrawlWork* class represents an action which executes for every single link.

```scala
package scala.demo

import scala.crawlgrowlcow.crawling.{CrawlResponse, CrawlResult, CrawlWork}
import scalaj.http.HttpRequest

object Demo extends App { // Starting object
  val crawl = CrawlGrowlCow(DemoWork, args)  // Start CrawlGrowlCow node 
 }                                          // by using your implemented class

class DemoWork extends CrawlWork { // Worker demo class

  override def work(fetched: CrawlResponse): CrawlResult = {
    println(fetched.toJson.toString) // prints content of a page
    
    //...
    // extract required data from response
    //...
    
    CrawlResult(fetched.toJson) // The result in JSON format has to be returned
                                //    As CrawlGrowlCow uses Json4S library
                                // Result should be putted as JValue class
  }
}
```
## Run master

## Run a slave



