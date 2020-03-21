import sbt._

object Dependencies {

  val akkaVersion = "2.6.3"
  val akkaHttpVersion = "10.1.11"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1"
  lazy val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
  lazy val akkaStreamTyped = "com.typesafe.akka" %% "akka-stream-typed" % akkaVersion
  lazy val akkaActorTyped = "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion
  lazy val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
  lazy val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
  lazy val akkaStreamTestkit = "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion
  lazy val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion
  lazy val scalaTestJsonAssert = "com.stephenn" %% "scalatest-json-jsonassert" % "0.0.5"

}
