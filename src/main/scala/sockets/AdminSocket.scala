package sockets

import actors.common.{ActorRegistry, UlakSystem}
import actors.Admin
import akka.NotUsed
import akka.actor.{PoisonPill, Props}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}

/**
  * Created by trozozti on 11/26/16.
  */
object AdminSocket extends UlakSystem {

  def newAdmin: Flow[Message, Message, NotUsed] = {
    // new connection - new admin actor
    val adminActor = system.actorOf(Props(new Admin(ActorRegistry.reception)))

    val incomingMessages: Sink[Message, NotUsed] =
      Flow[Message].map {
        // transform websocket message to domain message
        case TextMessage.Strict(text) => Admin.IncomingMessage(text)
        case _ => Admin.IncomingMessage("Unknown message format arrived")
      }.to(Sink.actorRef[Admin.IncomingMessage](adminActor, PoisonPill))

    val outgoingMessages: Source[Message, NotUsed] =
      Source.actorRef[Admin.OutgoingMessage](10, OverflowStrategy.fail)
        .mapMaterializedValue { outActor =>
          // give the admin actor a way to send messages out
          adminActor ! Admin.Connected(outActor)
          NotUsed
        }.map(
        // transform domain message to web socket message
        (outMsg: Admin.OutgoingMessage) => TextMessage(outMsg.text))

    // then combine both to a flow
    Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
  }

}
