package com.serviveragent.erasticounter

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.util.Timeout

import scala.concurrent.{ExecutionContextExecutor, Future}

// TODO: remove this class according to pxfnc's implementation.
class SiteCounter(implicit system: ActorSystem[CounterManager.Command], timeout: Timeout) {

  private implicit val executionContext: ExecutionContextExecutor = system.executionContext

  def countSite(): Future[Option[Int]] = {

    val value: Future[Int] = for {
      CounterManager.GetCounterRefReply(Some(counter)) <- system.ask(
        CounterManager.GetCounterRef("a", _: ActorRef[CounterManager.GetCounterRefReply])
      )
      Counter.ReadReply(count) <- counter ? Counter.Read
    } yield count

    //system.log.info(s"count: ${Await.result(value.map(Right(_)).recover(Left(_)), Duration.Inf)}")
    value.map(value => Some(value))
  }

}

object SiteCounter {
  def apply()(implicit system: ActorSystem[CounterManager.Command], timeout: Timeout): SiteCounter = new SiteCounter()
}
