lazy val scalatraVersion = "2.6.2"
lazy val jettyVersion = "9.3.0.M1"
lazy val slf4jVersion = "1.7.7"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.11.8",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "flightsearchsimulator",
    //autoScalaLibrary := false,
    crossPaths := false,
    publishMavenStyle := true,
    libraryDependencies ++= Seq(
      //scalaTest % Test,
      "org.scalatra" %% "scalatra" % scalatraVersion,
      "org.scalatra" %% "scalatra-json" % scalatraVersion,
      "org.json4s" %% "json4s-jackson" % "3.5.2",

      "org.eclipse.jetty" % "jetty-webapp" % jettyVersion,
      "org.eclipse.jetty" % "jetty-server" % jettyVersion,

      "org.slf4j" % "slf4j-api" % slf4jVersion,
      //"org.slf4j" % "slf4j-simple" % slf4jVersion,
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "org.scalaj" %% "scalaj-http" % "2.3.0",
      "org.elasticsearch.client" % "transport" % "6.0.0"
    )
  )
