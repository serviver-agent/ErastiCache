package com.serviveragent.erasticounter

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import com.serviveragent.erasticounter.SiteCounter.Command

import scala.collection.mutable.HashMap

object SiteCounter {

  type Url = String;

  def apply(state: HashMap[Url, Int] = HashMap.empty): Behavior[Command] = Behaviors.receiveMessage {

    case CountUpAt(url) => {
      state.updateWith(url) {
        case Some(count) => Some(count + 1)
        case None        => Some(1)
      }
      SiteCounter(state)
    }

    case ReadAt(url, replyTo) => {
      replyTo ! ReadAtReply(state.get(url))
      Behaviors.same
    }

  }

  sealed trait Command

  final case class CountUpAt(url: Url) extends Command

  final case class ReadAt(url: Url, replyTo: ActorRef[ReadAtReply]) extends Command

  object ReadAt {
    def create(url: Url)(replyTo: ActorRef[ReadAtReply]) = ReadAt(url: Url, replyTo: ActorRef[ReadAtReply])
  }

  final case class ReadAtReply(count: Option[Int])

}
