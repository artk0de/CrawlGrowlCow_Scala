lazy val root = (project in file(".")).
  settings(
    name := "CrawlGrowlCow",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.11.7"
  )

libraryDependencies ++= Seq(
  "org.jsoup" % "jsoup" % "1.8.3",
  "org.mongodb" %% "casbah" % "3.1.1",
  "org.slf4j" % "slf4j-log4j12" % "1.7.21",
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "org.json4s" %% "json4s-native" % "3.5.0"
)

val json4sNative = "org.json4s" %% "json4s-native" % "3.5.0"