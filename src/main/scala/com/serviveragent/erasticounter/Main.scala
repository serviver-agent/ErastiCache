package com.serviveragent.erasticounter

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.AskPattern._
import akka.util.Timeout
import scala.concurrent.{Await}
import scala.concurrent.duration._

object Main extends App {

  implicit val timeout: Timeout                         = 3.seconds
  implicit val system: ActorSystem[SiteCounter.Command] = ActorSystem(SiteCounter(), "system")

  system ! SiteCounter.CountUpAt("/")
  system ! SiteCounter.CountUpAt("/foo")
  system ! SiteCounter.CountUpAt("/bar")
  system ! SiteCounter.CountUpAt("/bar/baz")
  system ! SiteCounter.CountUpAt("/")
  system ! SiteCounter.CountUpAt("/foo")
  system ! SiteCounter.CountUpAt("/")
  system ! SiteCounter.CountUpAt("/foo")
  system ! SiteCounter.CountUpAt("/bar/baz")

  val fooCount = Await.result(system.ask(SiteCounter.ReadAt_("/foo")), Duration.Inf)
  println(s"/foo (${fooCount.count.getOrElse(-1)})")

  system.terminate()

}
