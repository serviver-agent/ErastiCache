// import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.HttpMethods._
import akka.actor.typed.scaladsl.adapter._
import akka.stream.ActorMaterializer

import scala.concurrent.Future

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.util.Timeout
import akka.actor.typed.scaladsl.AskPattern._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object Main extends App {

  implicit val timeout: Timeout                            = 3.seconds
  implicit val system: ActorSystem[CounterManager.Command] = ActorSystem(CounterManager(), "system")
  implicit val ec                                          = system.executionContext
  implicit val materializer                                = ActorMaterializer()(system.toClassic)

  system ! CounterManager.CreateCounter("serviver-agent.com")
  // system ! CounterManager.CreateCounter("serviver-agent.com/foo/bar")
  system ! CounterManager.CreateCounter("example.com")
  system ! CounterManager.IncrCounter("a")
  system ! CounterManager.IncrCounter("b")
  system ! CounterManager.IncrCounter("a")
  system ! CounterManager.ShowCounter("a")

  def fetchCount(ref: String): Future[Int] =
    for {
      CounterManager.GetCounterRefReply(Some(counter)) <- system.ask(
        CounterManager.GetCounterRef(ref, _: ActorRef[CounterManager.GetCounterRefReply])
      )
      Counter.ReadReply(count) <- counter ? Counter.Read
    } yield count

  val requestHandler: HttpRequest => HttpResponse = {
    case HttpRequest(GET, Uri.Path(ref), _, _, _) => {
      val count = Await.result(fetchCount(ref), 3.seconds) // ??????????????
      HttpResponse(entity = HttpEntity(ContentTypes.`text/html(UTF-8)`, count.toString))
    }

    case HttpRequest(POST, Uri.Path(ref), _, _, _) => {
      system ! CounterManager.IncrCounter(ref)
      HttpResponse()
    }

    case HttpRequest(GET, Uri.Path("/ping"), _, _, _) =>
      HttpResponse(entity = "PONG!")

    case HttpRequest(GET, Uri.Path("/crash"), _, _, _) =>
      sys.error("BOOM!")

    case r: HttpRequest =>
      r.discardEntityBytes() // important to drain incoming HTTP Entity stream
      HttpResponse(404, entity = "Unknown resource!")
  }

  val bindingFuture = Http(system.toClassic).bindAndHandleSync(requestHandler, "localhost", 8080)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  scala.io.StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind())                 // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

}
