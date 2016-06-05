import akka.actor.{Actor, ActorSystem}
import akka.actor.{ActorRef, Props}

case class Bill(cents: Int)
case object ClosingTime
case object CaffeineWithdrawalWarning

sealed trait CoffeeRequest
case object CappuccinoRequest extends CoffeeRequest
case object EspressoRequest extends CoffeeRequest

class Barista extends Actor {
  def receive: PartialFunction[Any, Unit] = {
    case CappuccinoRequest =>
      sender ! Bill(250)
      println("I have to prepare a cappuccino")
    case EspressoRequest =>
      sender ! Bill(200)
      println("Let's prepare an espresso")
    case ClosingTime =>
      context.system.shutdown()
  }
}

class CafeCustomer(caffeineSource: ActorRef) extends Actor {
  def receive: PartialFunction[Any, Unit] = {
    case CaffeineWithdrawalWarning =>
      caffeineSource ! EspressoRequest
    case Bill(cents) =>
      println(s"I have to pay $cents cents, or else!")
  }
}

object CoffeeHouse extends App {
  val system = ActorSystem("CoffeeHouse")
  val barista: ActorRef = system.actorOf(Props[Barista], "Barista")
  val customer: ActorRef = system.actorOf(Props(classOf[CafeCustomer], barista), "Customer")

  customer ! CaffeineWithdrawalWarning
  barista ! ClosingTime
}