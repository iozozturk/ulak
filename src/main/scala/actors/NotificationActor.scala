package actors

import actors.NotificationActor._
import akka.actor.{Actor, ActorLogging, ActorRef}

/**
  * Created by trozozti on 11/26/16.
  */
// @formatter:off
object NotificationActor {
  case class NotifyTeamForPlugin(teamId: String, plugin: String)
}
// @formatter:on

class NotificationActor(out: ActorRef) extends Actor with ActorLogging {
  log.debug("IS UP")

  override def receive: Receive = {
    case NotifyTeamForPlugin(teamId, plugin) =>
      log.info(teamId + plugin)
    case other =>
      log.info(s"Got unexpected type of msg: $other")
  }
}
