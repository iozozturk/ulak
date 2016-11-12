import akka.actor._
import akka.event.Logging

object Reception {
  case object Join
  case class Message(message: String)
}

class Reception extends Actor {
  import Reception._
  var users: Set[ActorRef] = Set.empty
  val log = Logging(context.system, this)

  def receive = {
    case Join =>
      users += sender()
      log.info("[Reception] Registering new user")
      // we also would like to remove the user when its actor is stopped
      context.watch(sender())

    case Terminated(user) =>
      log.info("[Reception] User is offline")
      users -= user

    case msg: Message =>
      log.info(s"[Reception] Sending msg for [${users.size}] users")
      users.foreach(_ ! msg)
  }
}
