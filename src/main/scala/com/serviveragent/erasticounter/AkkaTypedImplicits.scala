package com.serviveragent.erasticounter

import akka.actor.typed.ActorSystem
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait AkkaTypedImplicits {
  implicit val timeout: Timeout = 3.seconds
  implicit val system: ActorSystem[CounterManager.Command] = SiteCounterActorCreator.create(
    ActorSystem(CounterManager(), "system")
  )
  implicit val ec: ExecutionContext = system.executionContext
}

object AkkaTypedImplicits extends AkkaTypedImplicits
