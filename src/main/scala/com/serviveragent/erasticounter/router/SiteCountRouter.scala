package com.serviveragent.erasticounter.router

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.serviveragent.erasticounter.{AkkaTypedImplicits, SiteCounter}
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait SiteCountRouter {

  def countRoute: Route = {
    implicit val requestFormat: RootJsonFormat[CounterRequest] = jsonFormat1(CounterRequest)
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
    import AkkaTypedImplicits._
    for {
      value <- SiteCounter().countSite()
    } yield value.map(v => CounterResponse(v))
  }

}

case class CounterRequest(path: String)

case class CounterResponse(counts: Long)
