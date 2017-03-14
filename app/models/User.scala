package models

import play.api.libs.json.Json

case class User(
  firstName: String,
  lastName: String,
  email: String
)

object User {
  implicit val jsonFormat = Json.format[User]
}
