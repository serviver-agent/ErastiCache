package com.serviveragent.erasticounter.router

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

class HealthCheckRouterSpec extends WordSpec with Matchers with ScalatestRouteTest
  with HealthCheckRouter {

  "The HealthCheck router" should {
    "return status code 200 and a string OK" in {
      Get("/health") ~> healthCheckRoute ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "OK"
      }
    }
  }
}
