package actors.common

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

/**
  * Created by trozozti on 11/27/16.
  */
trait UlakSystem {

  implicit val system = ActorSystem("dashboard")
  implicit val materializer = ActorMaterializer()
}
