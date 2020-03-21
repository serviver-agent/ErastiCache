package com.serviveragent.erasticounter

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.adapter._
import akka.actor.typed.scaladsl.{ActorContext, Behaviors}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import akka.{Done, actor}
import com.serviveragent.erasticounter.router.RestRoute

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object Main extends App {

  private val host = "localhost"
  private val port = 8080

  private val timeout: Timeout = 3.seconds

  val behaviors = Behaviors.setup[Done] { ctx =>
    runRoute(ctx)
    Behaviors.receiveMessage {
      case Done =>
        Behaviors.stopped
    }
  }

  val system = ActorSystem(behaviors, "erasticounter-actor")

  private def runRoute(context: ActorContext[Done]): Unit = {
    implicit val untypedSystem: actor.ActorSystem = context.system.toClassic
    implicit val ec                               = untypedSystem.dispatcher
    implicit val materializer                     = ActorMaterializer()(untypedSystem) // TODO: don't use deprecated method.
    val route: Route                              = new RestRoute(untypedSystem, timeout).allRoutes
    val bindingFuture: Future[ServerBinding]      = Http().bindAndHandle(route, host, port)

    bindingFuture.onComplete {
      case Success(bound) =>
        println("server start.")
      case Failure(e) =>
        e.printStackTrace()
    }

  }

}
