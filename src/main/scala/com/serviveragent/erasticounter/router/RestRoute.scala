package com.serviveragent.erasticounter.router

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout

import scala.concurrent.ExecutionContext

class RestRoute(system: ActorSystem, timeout: Timeout) extends HealthCheckRouter
  with SiteCountRouter {

  private implicit val requestTimeout = timeout
  private implicit val executionContext: ExecutionContext = system.dispatcher

  def allRoutes: Route = {
    healthCheckRoute ~
      countRoute
  }

}
