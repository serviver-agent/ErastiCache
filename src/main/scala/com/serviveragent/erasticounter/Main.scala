package com.serviveragent.erasticounter

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.serviveragent.erasticounter.router.RestRoute

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.StdIn


object Main extends App {

  val host = "localhost"
  val port = 8080

  implicit val system = ActorSystem("erasticounter")
  implicit val ec = system.dispatcher
  val timeout: Timeout = 3.seconds

  val route: Route = new RestRoute(system, timeout).allRoutes

  implicit val materializer = ActorMaterializer()
  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(route, host, port)

  StdIn.readLine()
  bindingFuture.flatMap(_.unbind())
    .onComplete(_ => system.terminate())

  //  implicit val timeout: Timeout                            = 3.seconds
  //  implicit val system: ActorSystem[CounterManager.Command] = ActorSystem(CounterManager(), "system")
  //  implicit val ec                                          = system.executionContext
  //
  //  system ! CounterManager.CreateCounter("a")
  //  system ! CounterManager.CreateCounter("b")
  //  system ! CounterManager.IncrCounter("a")
  //  system ! CounterManager.IncrCounter("b")
  //  system ! CounterManager.IncrCounter("a")
  //  system ! CounterManager.ShowCounter("a")
  //
  //  val count: Future[Int] = for {
  //    CounterManager.GetCounterRefReply(Some(counter)) <- system.ask(
  //      CounterManager.GetCounterRef("a", _: ActorRef[CounterManager.GetCounterRefReply])
  //    )
  //    Counter.ReadReply(count) <- counter ? Counter.Read
  //  } yield count
  //
  //  system.log.info(s"count: ${Await.result(count.map(Right(_)).recover(Left(_)), Duration.Inf)}")
  //
  //  system.terminate()

}
