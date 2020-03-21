package com.serviveragent.erasticounter

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}
import akka.util.Timeout
import com.serviveragent.erasticounter.Counter._

import scala.collection.immutable.HashMap
import scala.concurrent.duration._
import scala.util.Success

object CounterManager {

  def apply(counters: HashMap[String, ActorRef[Counter.Command]] = HashMap.empty): Behavior[Command] =
    Behaviors.setup { context =>
      implicit val t: Timeout = 3.seconds

      Behaviors.receiveMessage {

        case ShowCounter(key) => {
          counters.get(key).foreach { counter =>
            context.ask(counter, Read) {
              case Success(ReadReply(count)) => {
                context.log.info(s"key: $key  count: $count")
                NoOp
              }
              case _ => {
                context.log.warn(s"response not received")
                NoOp
              }
            }
          }
          Behaviors.same
        }

        case DeleteCounter(key) => {
          counters.get(key) foreach { counter =>
            context.stop(counter)
            CounterManager(counters.removed(key))
          }
          CounterManager(counters.removed(key))
        }

        case IncrCounter(key) => {
          counters.get(key).foreach { counter =>
            counter ! Incr
          }
          Behaviors.same
        }

        case CreateCounter(key) => {
          val newCounter = context.spawnAnonymous(Counter())
          context.spawn(Counter(), key)
          CounterManager(counters + (key -> newCounter))
        }

        case GetCounterRef(key, replyTo) => {
          replyTo ! GetCounterRefReply(counters.get(key))
          Behaviors.same
        }

        case NoOp => Behaviors.same

      }

    }

  sealed trait Command

  final case class ShowCounter(key: String) extends Command

  final case class IncrCounter(key: String) extends Command

  final case class CreateCounter(key: String) extends Command

  final case class DeleteCounter(key: String) extends Command

  final case class GetCounterRef(key: String, replyTo: ActorRef[GetCounterRefReply]) extends Command

  final case object NoOp extends Command

  final case class GetCounterRefReply(counterRef: Option[ActorRef[Counter.Command]])

}
