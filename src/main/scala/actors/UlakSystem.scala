package actors

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

/**
  * Created by trozozti on 11/27/16.
  */
trait UlakSystem {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
}
