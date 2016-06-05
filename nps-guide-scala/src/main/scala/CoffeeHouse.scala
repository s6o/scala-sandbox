import akka.actor.{Actor, ActorSystem}
import akka.actor.{ActorRef, Props}

sealed trait CoffeeRequest
case object CappuccinoRequest extends CoffeeRequest
case object EspressoRequest extends CoffeeRequest

class Barista extends Actor {
  def receive: PartialFunction[Any, Unit] = {
    case CappuccinoRequest =>
      println("I have to prepare a cappuccino")
    case EspressoRequest =>
      println("Let's prepare an espresso")
  }
}

object CoffeeHouse extends App {
  val system = ActorSystem("CoffeeHouse")
  val barista: ActorRef = system.actorOf(Props[Barista], "Barista")
  barista ! CappuccinoRequest
  barista ! EspressoRequest
  println("I ordered a cappuccino and an espresso")
  system.shutdown()
}