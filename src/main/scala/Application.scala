import actors.common.{ActorRegistry, UlakSystem}
import actors.Reception
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import play.api.libs.json.Json
import sockets.{AdminSocket, UserSocket}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationLong

/**
  * Created by trozozti on 11/12/16.
  */
object Application extends UlakSystem {

  def main(args: Array[String]): Unit = {
    ActorRegistry.notificationActor
    val route =
      path("ulak") {
        get {
          //todo pass user cookie info & IP to newUser
          handleWebSocketMessages(UserSocket.newUser)
        }
      } ~
        path("admin") {
          get {
            handleWebSocketMessages(AdminSocket.newAdmin)
          }
        }

    val binding = Await.result(Http().bindAndHandle(route, "0.0.0.0", 8090), 3.seconds)

//    system.scheduler.schedule(10.seconds, 5.seconds, ActorRegistry.reception, Reception.Message(Json.obj("key"->"value").toString()))

    println("Started server at 0.0.0.0:8090")
  }
}
