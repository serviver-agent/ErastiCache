package com.serviveragent.erasticounter.router

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

trait HealthCheckRouter {

  def healthCheckRoute: Route = {
    path("health") {
      get {
        complete(HttpEntity(ContentTypes.`text/plain(UTF-8)`, "OK"))
      }
    }
  }

}
