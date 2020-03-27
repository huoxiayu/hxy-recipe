package com.hxy.recipe.actor

import java.util.concurrent.ThreadLocalRandom

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.hxy.recipe.util.Utils

object PingPongActorStart {

	def main(args: Array[String]): Unit = {
		val system = ActorSystem("PingPongSystem")
		val pong = system.actorOf(Props[Pong], name = "pong")
		val ping = system.actorOf(Props(new Ping(pong)), name = "ping")

		ping ! Start

		Utils.sleep(2L)
		system.terminate()
	}

}

case object PingMsg

case object PongMsg

case object Start

case object Stop

class Ping(pong: ActorRef) extends Actor {

	def receive = {
		case Start =>
			println("ping: receive start")
			pong ! PingMsg
		case PongMsg =>
			println("ping: receive pong")
			if (ThreadLocalRandom.current().nextInt(5) > 0) {
				sender ! PingMsg
			} else {
				sender ! Stop
			}
		case Stop =>
			println("ping: receive stop")
			context.stop(self)
	}
}

class Pong extends Actor {
	def receive = {
		case PingMsg =>
			println("pong: receive ping")
			sender ! PongMsg
		case Stop =>
			println("pong: receive stop")
			sender ! Stop
			context.stop(self)
	}
}