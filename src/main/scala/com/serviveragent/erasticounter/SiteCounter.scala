package com.serviveragent.erasticounter

import akka.actor.typed.ActorSystem
import akka.util.Timeout

import scala.concurrent.ExecutionContextExecutor

class SiteCounter(system: ActorSystem[CounterManager.Command], timeout: Timeout) {

  implicit val requestTimeout                             = timeout
  implicit def executionContext: ExecutionContextExecutor = system.executionContext

  def countSite(): Unit = {

    system ! CounterManager.CreateCounter("a")
    system ! CounterManager.CreateCounter("b")
    system ! CounterManager.IncrCounter("a")
    system ! CounterManager.IncrCounter("b")
    system ! CounterManager.IncrCounter("a")
    system ! CounterManager.ShowCounter("a")

    //    val count1: Future[Int] = for {
    //      CounterManager.GetCounterRefReply(Some(counter)) <- system.ask(
    //        CounterManager.GetCounterRef("a", _: ActorRef[CounterManager.GetCounterRefReply])
    //      )
    //      Counter.ReadReply(count) <- counter ? Counter.Read
    //    } yield count
    //  }
  }

}
