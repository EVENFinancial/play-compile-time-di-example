package controllers

import scala.concurrent.{ExecutionContext, Future}
import play.api._
import play.api.mvc._
import play.api.libs.json.Json

import clients.UserClient

class UserController(
  userClient: UserClient
)(implicit ec: ExecutionContext) extends Controller {

  def get(id: Int) = Action.async {
    userClient.getUser(id).map {
      case Some(user) => Ok(Json.toJson(user))
      case _ => NotFound
    }
  }

}
