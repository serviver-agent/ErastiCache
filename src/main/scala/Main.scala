import akka.actor.typed.{ActorRef, ActorSystem}
import akka.util.Timeout
import akka.actor.typed.scaladsl.AskPattern._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object Main extends App {

  implicit val timeout: Timeout = 3.seconds
  implicit val system: ActorSystem[CounterManager.Command] = ActorSystem(CounterManager(), "system")
  implicit val ec = system.executionContext

  system ! CounterManager.CreateCounter("a")
  system ! CounterManager.CreateCounter("b")
  system ! CounterManager.IncrCounter("a")
  system ! CounterManager.IncrCounter("b")
  system ! CounterManager.IncrCounter("a")
  system ! CounterManager.ShowCounter("a")

  val count: Future[Int] = for {
    CounterManager.GetCounterRefReply(Some(counter)) <- system.ask(CounterManager.GetCounterRef("a", _: ActorRef[CounterManager.GetCounterRefReply]))
    Counter.ReadReply(count) <- counter ? Counter.Read
  } yield count

  system.log.info(s"count: ${Await.result(count.map(Right(_)).recover(Left(_)), Duration.Inf)}")

  system.terminate()

}

