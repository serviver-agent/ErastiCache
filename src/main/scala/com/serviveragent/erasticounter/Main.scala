package com.serviveragent.erasticounter

import akka.actor
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import com.serviveragent.erasticounter.router.RestRoute

import scala.concurrent.Future
import scala.util.{Failure, Success}

object Main extends App {

  private val host = "localhost"
  private val port = 8080

  runRoute()

  private def runRoute(): Unit = {
    implicit val untypedSystem: actor.ActorSystem = AkkaTypedImplicits.system.toClassic
    implicit val ec                               = untypedSystem.dispatcher
    implicit val materializer                     = Materializer(untypedSystem)

    val route: Route                         = new RestRoute().allRoutes
    val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(route, host, port)

    bindingFuture.onComplete {
      case Success(bound) =>
        println("start erasticounter server.")
      case Failure(e) =>
        e.printStackTrace()
    }

  }

}
