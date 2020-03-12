package com.serviveragent.erasticounter

import akka.actor.ActorSystem
import akka.util.Timeout

class ErastiCounter(system: ActorSystem, timeout: Timeout) extends {

  implicit val requestTimeout = timeout
  implicit def executionContext = system.dispatcher

}
