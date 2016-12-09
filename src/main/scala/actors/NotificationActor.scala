package actors

import actors.NotificationActor._
import actors.common.ActorRegistry
import akka.actor.{Actor, ActorLogging, ActorRef}
import play.api.libs.json.Json

/**
  * Created by trozozti on 11/26/16.
  */
// @formatter:off
object NotificationActor {
  case class NotifyTeamForPlugin(teamId: String, plugin: String)
  def buildUpdateMessage(teamId:String, pluginType:String): String =
    Json.obj("type" -> "update","teamId" -> teamId,"pluginType" -> pluginType).toString()
}
// @formatter:on

class NotificationActor(out: ActorRef) extends Actor with ActorLogging {
  log.debug("IS UP")

  override def receive: Receive = {
    case NotifyTeamForPlugin(teamId, plugin) =>
      log.info(s"$teamId:$plugin")
      ActorRegistry.reception ! Reception.Message(buildUpdateMessage(teamId, plugin))
    case other =>
      log.info(s"Got unexpected type of msg: $other")
  }
}
