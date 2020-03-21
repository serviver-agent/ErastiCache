package com.serviveragent.erasticounter

import akka.actor.typed.ActorSystem

// TODO: remove this companion object according to pxfnc's implementation.
@Deprecated
object SiteCounterActorCreator {

  def create(system: ActorSystem[CounterManager.Command]): ActorSystem[CounterManager.Command] = {
    system ! CounterManager.CreateCounter("a")
    system ! CounterManager.CreateCounter("b")
    system ! CounterManager.IncrCounter("a")
    system ! CounterManager.IncrCounter("b")
    system ! CounterManager.IncrCounter("a")
    system ! CounterManager.ShowCounter("a")

    system
  }
}
