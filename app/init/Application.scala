package init

import play.api.ApplicationLoader.Context
import play.api.{BuiltInComponentsFromContext, LoggerConfigurator}
import play.api.inject.{Injector, NewInstanceInjector, SimpleInjector}
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.routing.Router

import clients._
import controllers._
import router.Routes

class ApplicationLoader extends play.api.ApplicationLoader {

  def load(context: Context) = {
    new ApplicationComponents(context)
      with DefaultClientComponents
  }.application

}

class ApplicationComponents(context: Context)
  extends BuiltInComponentsFromContext(context)
  with AhcWSComponents
{

  self: ClientComponents =>

  LoggerConfigurator(context.environment.classLoader).foreach { loggerConfigurator =>
    loggerConfigurator.configure(context.environment)
  }

  lazy val router: Router = new Routes(
    httpErrorHandler,
    new UserController(userClient)
  )

  override lazy val injector: Injector = new SimpleInjector(NewInstanceInjector) +
    router + crypto + httpConfiguration + wsApi + global

}
