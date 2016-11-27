package actors

import akka.actor.{Actor, ActorLogging}

/**
  * Created by trozozti on 11/26/16.
  */
class NotifierActor extends Actor with ActorLogging {

  case class Notification(teamId: String, plugin: String)

  override def receive: Receive = {
    case Notification(teamId, plugin) =>
      log.info(teamId + plugin)
    case a@_ =>
      log.info(s"Some other msg: $a")
  }
}
