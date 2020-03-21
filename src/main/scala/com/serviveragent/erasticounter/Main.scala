package com.serviveragent.erasticounter

import akka.actor
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.serviveragent.erasticounter.router.RestRoute

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object Main extends App {

  private val host = "localhost"
  private val port = 8080

  runRoute()

  private def runRoute(): Unit = {
    implicit val timeout: Timeout = 3.seconds
    implicit val system: ActorSystem[CounterManager.Command] = createSiteCounter(
      ActorSystem(CounterManager(), "system")
    )

    implicit val untypedSystem: actor.ActorSystem = system.toClassic
    implicit val ec                               = untypedSystem.dispatcher
    implicit val materializer                     = ActorMaterializer()(untypedSystem) // TODO: don't use deprecated method.

    val route: Route                         = new RestRoute().allRoutes
    val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(route, host, port)

    bindingFuture.onComplete {
      case Success(bound) =>
        println("start erasticounter server.")
      case Failure(e) =>
        e.printStackTrace()
    }

  }

  // TODO: remove this method according to pxfnc's implementation.
  private def createSiteCounter(system: ActorSystem[CounterManager.Command]): ActorSystem[CounterManager.Command] = {
    system ! CounterManager.CreateCounter("a")
    system ! CounterManager.CreateCounter("b")
    system ! CounterManager.IncrCounter("a")
    system ! CounterManager.IncrCounter("b")
    system ! CounterManager.IncrCounter("a")
    system ! CounterManager.ShowCounter("a")

    system
  }

}
