package clients

import play.api.libs.ws.ahc.AhcWSComponents

trait ClientComponents {

  def userClient: UserClient

}

trait DefaultClientComponents extends ClientComponents {

  self: AhcWSComponents =>

  lazy val userClient = new RandomUserClient(wsClient)

}
