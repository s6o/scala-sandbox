import akka.actor.{Actor, ActorSystem}
import akka.actor.{ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration._

case class Bill(cents: Int)
case object ClosingTime
case object CaffeineWithdrawalWarning

sealed trait CoffeeRequest
case object CappuccinoRequest extends CoffeeRequest
case object EspressoRequest extends CoffeeRequest

class Barista extends Actor {
  var cappuccinoCount: Int = 0
  var espressoCount: Int = 0

  def receive: PartialFunction[Any, Unit] = {
    case CappuccinoRequest =>
      sender ! Bill(250)
      cappuccinoCount += 1
      println(s"I have to prepare a cappuccino #$cappuccinoCount")
    case EspressoRequest =>
      sender ! Bill(200)
      espressoCount += 1
      println(s"Let's prepare an espresso: #$espressoCount")
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

  implicit val timeout = Timeout(2.second)
  implicit val ec = system.dispatcher

  val f: Future[Any] = barista ? CappuccinoRequest
  f.onSuccess {
    case Bill(cents) => println(s"Will pay $cents cents for a cappuccino")
  }

  customer ! CaffeineWithdrawalWarning
  barista ! ClosingTime
}