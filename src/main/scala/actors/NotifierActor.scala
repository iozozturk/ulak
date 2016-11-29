package actors

import actors.NotifierActor._
import akka.actor.{Actor, ActorLogging, ActorRef}

/**
  * Created by trozozti on 11/26/16.
  */
// @formatter:off
object NotifierActor {
  case class Notification(teamId: String, plugin: String)
}
// @formatter:on

class NotifierActor(out: ActorRef) extends Actor with ActorLogging {
  log.debug("IS UP")

  override def receive: Receive = {
    case Notification(teamId, plugin) =>
      log.info(teamId + plugin)
    case a@_ =>
      log.info(s"Got unexpected type of msg: $a")
  }
}
