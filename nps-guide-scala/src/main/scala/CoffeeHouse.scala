import Barista.ClosingTime
import CafeCustomer.CaffeineWithdrawalWarning
import ReceiptPrinter.PrintJob
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.pattern.AskTimeoutException

object Barista {
  case object EspressoRequest
  case object ClosingTime
  case class EspressoCup(state: EspressoCup.State)
  case class Receipt(amount: Int)
  case object ComeBackLater

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

object ReceiptPrinter {
  case class PrintJob(amount: Int)
  class PaperJamException(msg: String) extends Exception(msg)
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
      receipt.map((EspressoCup(Filled), _)).recover {
        case _: AskTimeoutException => ComeBackLater
      } pipeTo(sender)

    case ClosingTime =>
      context.system.shutdown()
  }
}

class Register extends Actor with ActorLogging {
  import akka.pattern.ask
  import akka.pattern.pipe
  import akka.util.Timeout
  import context.dispatcher
  import scala.concurrent.duration._
  import Barista._
  import Register._

  implicit val timeout = Timeout(2.seconds)
  var revenue = 0
  val prices = Map[Article, Int](Espresso -> 150, Cappuccino -> 250)
  val printer = context.actorOf(Props[ReceiptPrinter], "Printer")

  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)
    log.info(s"Restarted, and revenue is $revenue cents")
  }

  def receive: PartialFunction[Any, Unit] = {
    case Transaction(article) =>
      val price = prices(article)
      val requester = sender
      (printer ? PrintJob(price)).map((requester, _)).pipeTo(self)

    case (requester: ActorRef, receipt: Receipt) =>
      revenue += receipt.amount
      log.info(s"Revenue incremented to $revenue cents")
      requester ! receipt
  }
}

class ReceiptPrinter extends Actor with ActorLogging {
  import Barista._
  import ReceiptPrinter._

  var paperJam = false

  def createReceipt(price: Int): Receipt = {
    import util.Random
    if (Random.nextBoolean()) paperJam = true
    if (paperJam) {
      throw new PaperJamException("OMG, not again!")
    }
    Receipt(price)
  }

  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)
    log.info(s"Restarted, paper jam == $paperJam")
  }

  def receive: PartialFunction[Any, Unit] = {
    case PrintJob(amount) =>
      sender ! createReceipt(amount)
  }
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
    case ComeBackLater =>
      log.info("grumble, grumble")
  }
}

object CoffeeHouse extends App {
  val system = ActorSystem("CoffeeHouse")
  val barista: ActorRef = system.actorOf(Props[Barista], "Barista")

  val customerJohnny = system.actorOf(Props(classOf[CafeCustomer], barista), "Johnny")
  val customerAlina = system.actorOf(Props(classOf[CafeCustomer], barista), "Alina")

  customerJohnny ! CaffeineWithdrawalWarning
  customerAlina ! CaffeineWithdrawalWarning
  customerJohnny ! CaffeineWithdrawalWarning

  Thread.sleep(10000)
  barista ! ClosingTime
}