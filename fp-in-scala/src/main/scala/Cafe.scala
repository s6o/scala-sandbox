case class CreditCard(number: Long, cvv: Long, initialBalance: Long) {
  private var currentBalance: Long = initialBalance

  def balance: Long = currentBalance

  def charge(amount: Long): Unit = {
    currentBalance -= amount
  }
}

case class Coffee() {
  val price: Long = 100
}

class Cafe {

  def buyCoffee(cc: CreditCard): Coffee = {
    val cup = new Coffee()
    cc.charge(cup.price)
    cup
  }

}
