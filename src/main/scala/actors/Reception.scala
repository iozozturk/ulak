package actors

import akka.actor._
import akka.event.Logging

// @formatter:off
object Reception {
  case object Join
  case object JoinAdmin
  case class Message(message: String)
  case class AdminMessage(message: String)
}
// @formatter:on

class Reception extends Actor {

  import Reception._

  var users: Set[ActorRef] = Set.empty
  var admins: Set[ActorRef] = Set.empty
  val log = Logging(context.system, this)

  def receive = {
    case Join =>
      users += sender()
      log.info("[actors.Reception] Registering new user")
      // we also would like to remove the user when its actor is stopped
      context.watch(sender())

    case JoinAdmin =>
      admins += sender()
      log.info("[actors.Reception] Registering new admin")
      // we also would like to remove the user when its actor is stopped
      context.watch(sender())

    case Terminated(client) =>
      log.info("[actors.Reception] client is offline")
      users -= client
      admins -= client

    case msg: Message =>
      log.info(s"[actors.Reception] Sending Message for [${users.size}] users")
      users.foreach(_ ! msg)

    case msg: AdminMessage =>
      log.info(s"[actors.Reception] Sending AdminMessage for [${admins.size}] users")
      admins.foreach(_ ! msg)
  }
}
