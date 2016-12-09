package actors.common

import actors._
import akka.actor.{ActorRef, Props}

/**
  * Created by trozozti on 11/27/16.
  */
object ActorRegistry extends UlakSystem {
  println(">>>ACTORS UP")
  val reception: ActorRef = system.actorOf(Props(new Reception), "receptionActor")
  val notificationActor: ActorRef = system.actorOf(Props(new NotificationActor(reception)), "notificationActor")
  val metrics: ActorRef = system.actorOf(Props(new MetricsActor(reception)), "metricsActor")
  val monitoring: ActorRef = system.actorOf(Props(new MonitorActor(reception)), "monitoringActor")
}
