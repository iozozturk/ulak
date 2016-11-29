package actors

import akka.actor.{Actor, ActorLogging, ActorRef}
import play.api.libs.json.Json

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
  }

  def connected(outgoing: ActorRef): Receive = {
    receptionist ! Reception.Join

    {
      case IncomingMessage(text) =>
        //TODO To be impl
        log.info(s"[User] Incoming msg: $text")
        if ((Json.parse(text) \ "type").as[String] == "heartbeat")
          outgoing ! OutgoingMessage(Json.obj("type" -> "heartbeat", "msg" -> "pong").toString())

      case Reception.Message(text) =>
        log.info(s"[User] Sending Message:$text")
        outgoing ! OutgoingMessage(text)

      case t@_ =>
        log.warning(s"[User] Unexpected msg type:$t")
      //        receptionist ! Reception.Message(text)
    }
  }

}
