package com.serviveragent.erasticounter.router

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class RestRoute extends HealthCheckRouter with SiteCountRouter {

  def allRoutes: Route = {
    healthCheckRoute ~
      countRoute
  }

}
