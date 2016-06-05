import CafeCustomer.CaffeineWithdrawalWarning
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

object Barista {
  case object EspressoRequest
  case object ClosingTime
  case class EspressoCup(state: EspressoCup.State)
  case class Receipt(amount: Int)

  object EspressoCup {
    sealed trait State
    case object Clean extends State
    case object Filled extends State
    case object Dirty extends State
  }
}

object Register {
  sealed trait Article
  case object Espresso extends Article
  case object Cappuccino extends Article
  case class Transaction(article: Article)
}

object CafeCustomer {
  case object CaffeineWithdrawalWarning
}

class Barista extends Actor {
  import Barista._
  import Register._
  import EspressoCup._
  import context.dispatcher
  import akka.util.Timeout
  import akka.pattern.ask
  import akka.pattern.pipe
  import concurrent.duration._

  implicit val timeout = Timeout(2.seconds)
  val register = context.actorOf(Props[Register], "Register")

  def receive: PartialFunction[Any, Unit] = {
    case EspressoRequest =>
      val receipt = register ? Transaction(Espresso)
      receipt.map((EspressoCup(Filled), _)).pipeTo(sender)
    case ClosingTime =>
      context.stop(self)
  }
}

class Register extends Actor {
  import Register._
  import Barista._
  var revenue = 0
  val prices = Map[Article, Int](Espresso -> 150, Cappuccino -> 250)

  def receive: PartialFunction[Any, Unit] = {
    case Transaction(article) =>
      val price = prices(article)
      sender ! createReceipt(price)
      revenue += price
  }

  def createReceipt(price: Int): Receipt = Receipt(price)
}

class CafeCustomer(coffeeSource: ActorRef) extends Actor with ActorLogging {
  import CafeCustomer._
  import Barista._
  import EspressoCup._

  def receive: PartialFunction[Any, Unit] = {
    case CaffeineWithdrawalWarning =>
      coffeeSource ! EspressoRequest
    case (EspressoCup(Filled), Receipt(amount)) =>
      log.info(s"yay, caffeine for ${self}!")
  }
}

object CoffeeHouse extends App {
  val system = ActorSystem("CoffeeHouse")
  val barista: ActorRef = system.actorOf(Props[Barista], "Barista")

  val customerJohnny = system.actorOf(Props(classOf[CafeCustomer], barista), "Johnny")
  val customerAlina = system.actorOf(Props(classOf[CafeCustomer], barista), "Alina")

  customerJohnny ! CaffeineWithdrawalWarning
  customerAlina ! CaffeineWithdrawalWarning

  Thread.sleep(5000)
  system.shutdown()
}