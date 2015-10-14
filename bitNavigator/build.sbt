name := """bitNavigator"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.typesafe.play" %% "play-mailer" % "2.4.1",
  "org.apache.directory.studio" % "org.apache.commons.io" % "2.4",
  "com.cloudinary" % "cloudinary" % "1.0.14",
  "com.paypal.sdk" % "rest-api-sdk" % "1.2.0"
)

lazy val myProject = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
