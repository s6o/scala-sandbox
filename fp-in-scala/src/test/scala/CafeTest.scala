import org.specs2.mutable._

class CafeTest extends Specification {

  "Cafe#buyCoffee" should {
    "decrement Credit Card balance" in {
      val cafe = new Cafe()
      val cc1 = CreditCard(1234567812345678L, 111L, 0L)
      val (coffee, charge) = cafe.buyCoffee(cc1)
      val cc2 = Payments().process(cc1, charge)

      cc2.balance must beEqualTo(-100L)
    }
  }

}
