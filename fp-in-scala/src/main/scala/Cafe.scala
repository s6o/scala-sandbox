case class CreditCard(number: Long, cvv: Long, balance: Long)

case class Charge(cc: CreditCard, amount: Long) {
  def combine(other: Charge): Charge = {
    if (cc == other.cc) {
      Charge(cc, amount + other.amount)
    } else {
      throw new Exception("Can't combine charge to different cards")
    }
  }
}

case class Payments() {
  def process(cc: CreditCard, charge: Charge): CreditCard = {
    cc.copy(balance = cc.balance - charge.amount)
  }
}

case class Coffee() {
  val price: Long = 100
}

class Cafe {

  def buyCoffee(cc: CreditCard): (Coffee, Charge) = {
    val cup = new Coffee()
    (cup, Charge(cc, cup.price))
  }

}
