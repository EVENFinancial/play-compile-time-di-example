package test

import scala.concurrent.{ExecutionContext, Future}
import play.api.{ApplicationLoader, Environment}
import play.api.ApplicationLoader.Context

import clients._
import init.ApplicationComponents
import models.User

class TestUserClient(
  users: (Int, User)*
) extends UserClient {

  private val usersMap = Map(users:_*)

  def getUser(id: Int)(implicit ec: ExecutionContext): Future[Option[User]] = {
    Future.successful(usersMap.get(id))
  }

}

class TestApplicationComponents(
  context: Context = ApplicationLoader.createContext(Environment.simple()),
  val userClient: UserClient
) extends ApplicationComponents(context)
  with ClientComponents
