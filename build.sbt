lazy val root = (project in file(".")).
  settings(
    name := "CrawlGrowlCow",
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.11.7"
  )

libraryDependencies ++= {
  val akkaV = "2.3.9"
  val sprayV = "1.3.3"
  Seq(
    "org.jsoup"           %   "jsoup"         % "1.8.3",
    "org.mongodb.scala"   %   "mongo-scala-driver_2.11" % "1.1.1",
    "org.slf4j"           %   "slf4j-log4j12" % "1.7.21",
    "org.scalaj"          %%  "scalaj-http"   % "2.3.0",
    "org.json4s"          %%  "json4s-native" % "3.5.0",

    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-client"  % sprayV,
    "io.spray"            %%  "spray-json"    % "1.3.2",
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test",
    "commons-validator"   %   "commons-validator" % "1.5+"
  )
}

//val json4sNative = "org.json4s" %% "json4s-native" % "{latestVersion}"
