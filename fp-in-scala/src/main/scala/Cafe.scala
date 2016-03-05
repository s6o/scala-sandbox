case class CreditCard(number: Long, cvv: Long, balance: Long)

case class Charge(cc: CreditCard, amount: Long) {
  def combine(other: Charge): Option[Charge] = {
    if (cc == other.cc) {
      Some(Charge(cc, amount + other.amount))
    } else {
      None
    }
  }
}

object Payments {
  private def coalesce(charges: List[Charge]): List[Charge] = charges.groupBy(_.cc).values.map(
      _.reduce((c1, c2) => c1.combine(c2) match { case Some(c) => c case _ => Charge(c1.cc, 0) })
    ).toList

  def process(charge: Charge): CreditCard = {
    charge.cc.copy(balance = charge.cc.balance - charge.amount)
  }

  def processBatch(charges: List[Charge]): List[CreditCard] = coalesce(charges).map(process(_))
}

case class Coffee() {
  val price: Long = 100
}

class Cafe {

  def buyCoffee(cc: CreditCard): (Coffee, Charge) = {
    val cup = new Coffee()
    (cup, Charge(cc, cup.price))
  }

  def buyCoffees(cc: CreditCard, n: Int): (List[Coffee], Charge) = {
    val purchases: List[(Coffee, Charge)] = List.fill(n)(buyCoffee(cc))
    val (coffees, charges) = purchases.unzip
    (coffees, charges.reduce((c1, c2) => c1.combine(c2) match { case Some(c) => c case _ => Charge(c1.cc, 0) } ))
  }

}
