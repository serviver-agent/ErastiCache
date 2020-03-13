package com.serviveragent.erasticounter

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.InvalidActorNameException

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import org.scalatest._

class PathSpec extends FlatSpec with Matchers with BeforeAndAfterAll {

  object DummyActor {
    def apply(): Behavior[Command] = Behaviors.receiveMessage {
      case Command => DummyActor()
    }
    sealed trait Command
    final case object Command extends Command
  }

  val testKit                   = ActorTestKit()
  override def afterAll(): Unit = testKit.shutdownTestKit()

  "actor path" should "not accept hoge" in {

    testKit.spawn(DummyActor.apply(), "hoge")

  }

  "actor path" should "not accept ho/ge" in {

    assertThrows[InvalidActorNameException] {
      testKit.spawn(
        DummyActor.apply(),
        "ho/ge"
      )
    }

  }

}
