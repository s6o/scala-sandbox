case class CreditCard(number: Long, cvv: Long) {
  private var currentBalance: Long = 0L

  def balance: Long = currentBalance

  def update(balance: Long): Unit = {
    currentBalance = balance
  }
}

case class Payments() {
  def charge(cc: CreditCard, coffee: Coffee): Unit = {
    /*
    Given the case class below:
      case class CreditCard(number: Long, cvv: Long, balance: Long)

    We' could do:
      cc.copy(balance = cc.balance - coffee.price)

    and return a new immutable instance of CreditCard, but given that the buyCoffee in listing 1.2.
    only calls Payments::charge and does not keep the returned value this is not an option
    */
    cc.update(cc.balance - coffee.price)
  }
}

case class Coffee() {
  val price: Long = 100
}

class Cafe {

  def buyCoffee(cc: CreditCard, p: Payments): Coffee = {
    val cup = new Coffee()
    p.charge(cc, cup)
    cup
  }

}
