name := "ulak"

version := "1.0"

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.4.12",
  "com.typesafe.akka" %% "akka-actor" % "2.4.12",
  "com.typesafe.akka" % "akka-http-core_2.11" % "3.0.0-RC1",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.11"
)
    