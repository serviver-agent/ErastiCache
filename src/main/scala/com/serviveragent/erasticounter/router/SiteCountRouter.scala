package com.serviveragent.erasticounter.router

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait SiteCountRouter {

  private implicit val requestFormat = jsonFormat1(CounterRequest)

  def countRoute: Route = {
    path("counter") {
      post {
        entity(as[CounterRequest]) { params =>
          responseWithCount(params)
        }
      }
    }
  }

  private def responseWithCount(request: CounterRequest): Route = {
    implicit val responseFormat = jsonFormat1(CounterResponse)
    onComplete(getSiteCount(request)) {
      case Success(count) => complete(count)
      case Failure(e)     =>
        // TODO: emit log that indicates the error is occurred.
        complete(InternalServerError)
    }
  }

  private def getSiteCount(request: CounterRequest): Future[Option[CounterResponse]] = {
    Future.successful(Some(CounterResponse(4500)))
  }

}

case class CounterRequest(path: String)

case class CounterResponse(counts: Long)
