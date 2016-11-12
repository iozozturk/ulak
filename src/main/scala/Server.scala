import akka.NotUsed
import akka.actor.{ActorSystem, PoisonPill, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.stream.{ActorMaterializer, OverflowStrategy}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationLong

/**
  * Created by trozozti on 11/12/16.
  */
object Server {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()

    val reception = system.actorOf(Props(new Reception), "reception")

    def newUser: Flow[Message, Message, NotUsed] = {
      // new connection - new user actor
      val userActor = system.actorOf(Props(new User(reception)))

      val incomingMessages: Sink[Message, NotUsed] =
        Flow[Message].map {
          // transform websocket message to domain message
          case TextMessage.Strict(text) => User.IncomingMessage(text)
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

    val route =
      path("ulak") {
        get {
          handleWebSocketMessages(newUser)
        }
      }

    val binding = Await.result(Http().bindAndHandle(route, "127.0.0.1", 8080), 3.seconds)

    system.scheduler.schedule(10.seconds, 2.seconds, reception, Reception.Message("ismet"))

    println("Started server at 127.0.0.1:8080")
  }
}
