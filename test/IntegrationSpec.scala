package test

import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.libs.json._

import models.User

class IntegrationSpec extends PlaySpec with OneServerPerSuite {

  override implicit lazy val app = {
    new TestApplicationComponents(
      userClient = new TestUserClient(
        1 -> User("Alice", "Smith", "asmith@example.com")
      )
    )
  }.application

  val resource = controllers.routes.UserController

  "GET /users/:id" should {

    "respond with a 404 Not Found if the user ID cannot be found" in {
      val response = await { wsCall(resource.get(2)).get }
      response.status mustBe NOT_FOUND
    }

    "respond with Alice's information, if her ID is specified" in {
      val response = await { wsCall(resource.get(1)).get }
      response.status mustBe OK
      (response.json \ "firstName").as[String] mustBe "Alice"
      (response.json \ "lastName").as[String] mustBe "Smith"
      (response.json \ "email").as[String] mustBe "asmith@example.com"
    }

  }

}
