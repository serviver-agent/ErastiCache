package com.serviveragent.erasticounter.router

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.stephenn.scalatest.jsonassert.JsonMatchers
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SiteCounterRouterSpec
    extends AnyWordSpec
    with Matchers
    with JsonMatchers
    with ScalatestRouteTest
    with SiteCountRouter {

  "The SiteCounter router" should {
    "return status code 200 and the number of times the web site is accessed as JSON" in {
      val requestJson =
        """
          |{
          |  "path": "blog.serviver-agent.com/posts/2000"
          |}
          |""".stripMargin

      val expectedJson =
        """
          |{
          |  "counts": 4500
          |}
          |""".stripMargin

      Post("/counter").withEntity(ContentTypes.`application/json`, requestJson) ~>
        countRoute ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] should matchJson(expectedJson)
        responseEntity.contentType shouldEqual ContentTypes.`application/json`
      }
    }
  }

}
