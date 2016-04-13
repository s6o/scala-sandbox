import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

case class Water(temperature: Int)

case class GridingException(msg: String) extends Exception(msg)
case class FrothingException(msg: String) extends Exception(msg)
case class WaterBoilingException(msg: String) extends Exception(msg)
case class BrewingException(msg: String) extends Exception(msg)

object Coffee {

  type CoffeeBeans = String
  type GroundCoffee = String
  type Milk = String
  type FrothedMilk = String
  type Espresso = String
  type Cappuccino = String

  def grind(beans: CoffeeBeans): Future[GroundCoffee] = Future {
    println("start grinding")
    Thread.sleep(Random.nextInt(2000))
    if (beans == "backed beans") throw GridingException("are you joking?")
    println("grinding finished")
    s"ground coffee of $beans"
  }

  def heatWater(water: Water): Future[Water] = Future {
    println("heating the water now")
    Thread.sleep(Random.nextInt(2000))
    println("hot, it's hot!")
    water.copy(temperature = 85)
  }

  def frothMilk(milk: Milk): Future[FrothedMilk] = Future {
    println("milk forthing system engaged")
    Thread.sleep(Random.nextInt(2000))
    println("shutting down milk frothing system")
    s"frothed $milk"
  }

  def brew(coffee: GroundCoffee, heatedWater: Water): Future[Espresso] = Future {
    println("happy brewing :)")
    Thread.sleep(Random.nextInt(2000))
    println("it's brewed")
    "espresso"
  }

  def combine(espresso: Espresso, frothedMilk: FrothedMilk): Cappuccino = "cappuccino"

  def prepareCappuccinoSequentially(): Future[Cappuccino] = {
    for {
      ground <- grind("arabica beans")
      water <- heatWater(Water(20))
      foam <- frothMilk("milk")
      espresso <- brew(ground, water)
    } yield combine(espresso, foam)
  }

  def prepareCappuccino(): Future[Cappuccino] = {
    val groundCoffee = grind("arabica beans")
    val heatedWater = heatWater(Water(20))
    val frothedMilk = frothMilk("milk")

    for {
      ground <- groundCoffee
      water <- heatedWater
      foam <- frothedMilk
      espresso <- brew(ground, water)
    } yield combine(espresso, foam)
  }

  def main(args: Array[String]): Unit = {
    println("\nMaking a cappuccino, sequentially ...")

    val f1 = Coffee.prepareCappuccinoSequentially()
    f1.onComplete {
      case Success(cappuccino) => println(cappuccino)
      case Failure(ex) => println(ex.getMessage)
    }

    Await.ready(f1, 20.seconds)

    println("\nMaking a cappuccino ...")

    val f2 = Coffee.prepareCappuccino()
    f2.onComplete {
      case Success(cappuccino) => println(cappuccino)
      case Failure(ex) => println(ex.getMessage)
    }

    Await.ready(f2, 20.seconds)
  }

}
