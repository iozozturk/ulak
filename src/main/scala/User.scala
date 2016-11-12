import akka.actor.{Actor, ActorRef}
import akka.event.Logging

object User {
  case class Connected(outgoing: ActorRef)
  case class IncomingMessage(text: String)
  case class OutgoingMessage(text: String)
}

class User(receptionist: ActorRef) extends Actor {
  import User._
  val log = Logging(context.system, this)

  def receive = {
    case Connected(outgoing) =>
      log.info("[User] New user connected")
      context.become(connected(outgoing))

    case OutgoingMessage(text) =>
      log.info("[User] Sending [Outgoing] msg from top")
      receptionist ! Reception.Message(text)
  }

  def connected(outgoing: ActorRef): Receive = {
    receptionist ! Reception.Join

    {
      case IncomingMessage(text) =>
        //TODO To be impl
        log.info(s"[User] Incoming msg: $text")

      case Reception.Message(text) =>
        log.info("[User] Sending [Reception] msg from bottom")
        outgoing ! OutgoingMessage(text)

      case OutgoingMessage(text) =>
        log.info("[User] Sending [Outgoing] msg from bottom ")
        receptionist ! Reception.Message(text)
    }
  }

}
