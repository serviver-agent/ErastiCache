package com.serviveragent.erasticounter.router

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.serviveragent.erasticounter.{CounterManager, SiteCounter}
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait SiteCountRouter {

  def countRoute(implicit actorSystem: ActorSystem[CounterManager.Command], timeout: Timeout): Route = {
    implicit val requestFormat: RootJsonFormat[CounterRequest] = jsonFormat1(CounterRequest)
    path("counter") {
      post {
        entity(as[CounterRequest]) { params =>
          responseWithCount(params)
        }
      }
    }
  }

  private def responseWithCount(
      request: CounterRequest
  )(implicit actorSystem: ActorSystem[CounterManager.Command], timeout: Timeout): Route = {
    implicit val responseFormat = jsonFormat1(CounterResponse)
    onComplete(getSiteCount(request)) {
      case Success(count) => complete(count)
      case Failure(e)     =>
        // TODO: emit log that indicates the error is occurred.
        complete(InternalServerError)
    }
  }

  private def getSiteCount(
      request: CounterRequest
  )(implicit actorSystem: ActorSystem[CounterManager.Command], timeout: Timeout): Future[Option[CounterResponse]] = {
    implicit val ec: ExecutionContext = actorSystem.executionContext
    for {
      value <- SiteCounter().countSite()
    } yield value.map(v => CounterResponse(v))
  }

}

case class CounterRequest(path: String)

case class CounterResponse(counts: Long)
