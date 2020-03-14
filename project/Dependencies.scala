import sbt._

object Dependencies {

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1"
  lazy val akkaActorTyped = "com.typesafe.akka" %% "akka-actor-typed" % "2.5.26"
  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.1.11"
  lazy val akkaStream = "com.typesafe.akka" %% "akka-stream" % "2.5.26"
  lazy val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11"
  lazy val akkaStreamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.26"
  lazy val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % "10.1.11"
  lazy val scalaTestJsonAssert = "com.stephenn" %% "scalatest-json-jsonassert" % "0.0.5"

}
