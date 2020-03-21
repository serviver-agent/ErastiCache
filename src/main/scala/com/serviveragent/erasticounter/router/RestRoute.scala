package com.serviveragent.erasticounter.router

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.serviveragent.erasticounter.CounterManager

class RestRoute(implicit system: ActorSystem[CounterManager.Command], timeout: Timeout)
    extends HealthCheckRouter
    with SiteCountRouter {

//  private implicit val actorSystem: ActorSystem[CounterManager.Command] = system
//  private implicit val actorTimeout                                     = timeout

  def allRoutes: Route = {
    healthCheckRoute ~
      countRoute
  }

}
