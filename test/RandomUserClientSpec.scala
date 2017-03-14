package test

import org.scalatest.{WordSpec, MustMatchers}
import play.core.server.Server
import play.api.routing.sird._
import play.api.mvc._
import play.api.test.Helpers.{GET => _, _}
import play.api.test.WsTestClient
import play.api.libs.json._
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import clients.RandomUserClient
import models.User

class RandomUserClientSpec extends WordSpec with MustMatchers {

  def withClient(block: RandomUserClient => Unit) {
    Server.withRouter() {
      case GET(p"/api" ? q"seed=$id") => Action { request =>
        id match {
          case "1" => Results.Ok(Json.parse(randomUserJson))
          case _ => Results.NotFound
        }
      }
    } { implicit port =>
      WsTestClient.withClient { wsTestClient =>
        val client = new RandomUserClient(wsTestClient, "")
        block(client)
      }
    }
  }

  "RandomUserClient.getUser" should {

    "return a None if the response is not a 200" in {
      withClient { client =>
        val result = await { client.getUser(2) }
        result mustBe None
      }
    }

    "return a User if the response is not a 200" in {
      withClient { client =>
        val user = await { client.getUser(1) }.get
        user.firstName mustBe "janique"
        user.lastName mustBe "costa"
        user.email mustBe "janique.costa@example.com"
      }
    }

  }

  val randomUserJson =
"""
{
  "results": [{
    "gender": "male",
    "name": {
      "title": "mr",
      "first": "janique",
      "last": "costa"
    },
    "location": {
      "street": "5236 rua tiradentes ",
      "city": "bragança",
      "state": "alagoas",
      "postcode": 81417
    },
    "email": "janique.costa@example.com",
    "login": {
      "username": "bluerabbit924",
      "password": "thrasher",
      "salt": "c7QvzaON",
      "md5": "b37c938f78f4789510051ce9068f617c",
      "sha1": "67e4995483263d9e9c7b521b80343479cdde43a8",
      "sha256": "106255c234f8633d3f39f5f172147e238d5a5525713308fa0bda09606dda74b9"
    },
    "dob": "1969-10-22 10:35:24",
    "registered": "2014-09-22 22:38:28",
    "phone": "(48) 4518-1459",
    "cell": "(22) 3632-3660",
    "id": {
      "name": "",
      "value": null
    },
    "picture": {
      "large": "https://randomuser.me/api/portraits/men/97.jpg",
      "medium": "https://randomuser.me/api/portraits/med/men/97.jpg",
      "thumbnail": "https://randomuser.me/api/portraits/thumb/men/97.jpg"
    },
    "nat": "BR"
  }],
  "info": {
    "seed": "1",
    "results": 1,
    "page": 1,
    "version": "1.1"
  }
}
"""

}
