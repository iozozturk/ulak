package sockets

import actors.common.{ActorRegistry, UlakSystem}
import actors.User
import akka.NotUsed
import akka.actor.{PoisonPill, Props}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}

/**
  * Created by trozozti on 11/26/16.
  */
object UserSocket extends UlakSystem {

  def newUser: Flow[Message, Message, NotUsed] = {
    // new connection - new user actor
    val userActor = system.actorOf(Props(new User(ActorRegistry.reception)))

    val incomingMessages: Sink[Message, NotUsed] =
      Flow[Message].map {
        // transform websocket message to domain message
        case TextMessage.Strict(text) => User.IncomingMessage(text)
        case _ => User.IncomingMessage("Unknown message format arrived")
      }.to(Sink.actorRef[User.IncomingMessage](userActor, PoisonPill))

    val outgoingMessages: Source[Message, NotUsed] =
      Source.actorRef[User.OutgoingMessage](10, OverflowStrategy.fail)
        .mapMaterializedValue { outActor =>
          // give the user actor a way to send messages out
          userActor ! User.Connected(outActor)
          NotUsed
        }.map(
        // transform domain message to web socket message
        (outMsg: User.OutgoingMessage) => TextMessage(outMsg.text))

    // then combine both to a flow
    Flow.fromSinkAndSource(incomingMessages, outgoingMessages)
  }

}
