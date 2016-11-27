package actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.event.Logging

// @formatter:off
object User {
  case class Connected(outgoing: ActorRef)
  case class IncomingMessage(text: String)
  case class OutgoingMessage(text: String)
}
// @formatter:on

class User(receptionist: ActorRef) extends Actor with ActorLogging {

  import User._

  def receive = {
    case Connected(outgoing) =>
      log.info("[actors.User] New user connected")
      context.become(connected(outgoing))

    case OutgoingMessage(text) =>
      log.info("[actors.User] Sending [Outgoing] msg from top")
      receptionist ! Reception.Message(text)
  }

  def connected(outgoing: ActorRef): Receive = {
    receptionist ! Reception.Join

    {
      case IncomingMessage(text) =>
        //TODO To be impl
        log.info(s"[actors.User] Incoming msg: $text")

      case Reception.Message(text) =>
        log.info("[actors.User] Got Message from bottom")
        log.info("[actors.User] Sending Message from bottom")
        outgoing ! OutgoingMessage(text)

      case OutgoingMessage(text) =>
        log.info("[actors.User] Sending [Outgoing] msg from bottom ")
        receptionist ! Reception.Message(text)
    }
  }

}
