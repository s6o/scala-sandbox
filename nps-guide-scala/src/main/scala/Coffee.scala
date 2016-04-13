import scala.util.Try

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

  def grind(beans: CoffeeBeans): GroundCoffee = s"ground coffee of $beans"

  def heatWater(water: Water): Water = water.copy(temperature = 85)

  def frothMilk(milk: Milk): FrothedMilk = s"frothed $milk"

  def brew(coffee: GroundCoffee, heatedWater: Water): Espresso = "espresso"

  def combine(espresso: Espresso, frothedMilk: FrothedMilk): Cappuccino = "cappuccino"

  def prepareCappuccino(): Try[Cappuccino] = for {
    ground <- Try(grind("arabica beans"))
    water <- Try(heatWater(Water(25)))
    espresso <- Try(brew(ground, water))
    foam <- Try(frothMilk("milk"))
  } yield combine(espresso, foam)

}
