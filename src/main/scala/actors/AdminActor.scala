package actors

import akka.actor.{Actor, ActorLogging, ActorRef}

// @formatter:off
object Admin {
  case class Connected(outgoing: ActorRef)
  case class IncomingMessage(text: String)
  case class OutgoingMessage(text: String)
}
// @formatter:on

class Admin(receptionist: ActorRef) extends Actor with ActorLogging {

  import Admin._

  def receive = {
    case Connected(outgoing) =>
      log.info("[Admin] New admin connected")
      context.become(connected(outgoing))

    case OutgoingMessage(text) =>
      log.info("[admin] Sending [Outgoing] msg from top")
      receptionist ! Reception.AdminMessage(text)
  }

  def connected(outgoing: ActorRef): Receive = {
    receptionist ! Reception.JoinAdmin

    {
      case IncomingMessage(text) =>
        //TODO To be impl
        log.info(s"[admin] Incoming msg: $text")

      case Reception.AdminMessage(text) =>
        log.info("[admin] Got Message from bottom")
        log.info("[admin] Sending Message from bottom")
        outgoing ! OutgoingMessage(text)

      case OutgoingMessage(text) =>
        log.info("[admin] Sending [Outgoing] msg from bottom ")
        receptionist ! Reception.AdminMessage(text)
    }
  }

}
