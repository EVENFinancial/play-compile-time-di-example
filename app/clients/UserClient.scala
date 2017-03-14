package clients

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.JsValue
import play.api.libs.ws.WSClient

import models.User

trait UserClient {

  def getUser(id: Int)(implicit ec: ExecutionContext): Future[Option[User]]

}

class RandomUserClient(
  ws: WSClient,
  baseUrl: String = "https://randomuser.me"
) extends UserClient {

  def getUser(id: Int)(implicit ec: ExecutionContext): Future[Option[User]] = {
    ws.url(baseUrl + "/api?seed=" + id).get().map { response =>
      if (response.status == 200) {
        (response.json \ "results")(0).asOpt[JsValue].flatMap { result =>
          for {
            firstName <- (result \ "name" \ "first").asOpt[String]
            lastName <- (result \ "name" \ "last").asOpt[String]
            email <- (result \ "email").asOpt[String]
          } yield User(firstName, lastName, email)
        }
      } else None
    }
  }

}
