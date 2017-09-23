name := """coffeebear"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  evolutions,
  ws,
  "org.scalikejdbc" %% "scalikejdbc" % "2.1.2",
  "mysql" % "mysql-connector-java" % "5.1.29",
  "ch.qos.logback"    %   "logback-classic" % "1.1.1",
  "org.json4s" %% "json4s-native" % "3.3.0",
  "org.json4s" %% "json4s-jackson" % "3.3.0",
  "io.spray" % "spray-json_2.11" % "1.3.3",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

