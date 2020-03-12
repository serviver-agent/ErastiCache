package com.serviveragent.etasticounter

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

object Counter {

  def apply(state: Int = 0): Behavior[Command] = Behaviors.receiveMessage {
    case Incr => Counter(state + 1)
    case Decr => Counter(state - 1)
    case Read(replyTo) => {
      replyTo ! ReadReply(state)
      Behaviors.same
    }
    case Reset => Counter(0)

  }

  sealed trait Command

  final case class ReadReply(count: Int)

  final case class Read(replyTo: ActorRef[ReadReply]) extends Command

  final case object Incr extends Command

  final case object Decr extends Command

  final case object Reset extends Command

}
