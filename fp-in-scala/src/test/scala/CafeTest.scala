import org.specs2.mutable._

class CafeTest extends Specification {

  "Cafe#buyCoffee" should {
    "decrement Credit Card balance" in {
      val cc = CreditCard(1234567812345678L, 111L)
      val cafe = new Cafe()
      cafe.buyCoffee(cc, Payments())
      cc.balance must beEqualTo(-100L)
    }
  }

}
