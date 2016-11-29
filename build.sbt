import sbt.Keys.javaOptions

name := "ulak"

version := "1.0"

scalaVersion := "2.11.8"
val akkaVersion = "2.4.13"

val project = Project(
  id = "ulak",
  base = file(".")
).settings(
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-metrics" % akkaVersion,
    "com.typesafe.akka" %% "akka-remote" % akkaVersion,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % "10.0.0",
    "io.kamon" % "sigar-loader" % "1.6.6-rev002",
    "de.heikoseeberger" %% "akka-http-play-json" % "1.10.1"
  ),
  javaOptions in run ++= Seq("-Xms128m", "-Xmx1024m", "-Djava.library.path=./target/native"),
  Keys.fork in run := true,
  mainClass in(Compile, run) := Some("Application")
)