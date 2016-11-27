package actors

import akka.actor.{ActorRef, Props}

/**
  * Created by trozozti on 11/27/16.
  */
object ActorRegistry extends UlakSystem {
  val reception: ActorRef = system.actorOf(Props(new Reception), "reception")

}
