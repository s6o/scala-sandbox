import org.specs2.mutable._

class CafeTest extends Specification {

  "Cafe#buyCoffee" should {
    "decrement Credit Card balance once for 1 coffee" in {
      val cafe = new Cafe()
      val cc1 = CreditCard(1234567812345678L, 111L, 1000L)
      val (coffee, charge) = cafe.buyCoffee(cc1)
      val cc2 = Payments().process(cc1, charge)

      cc2.balance must beEqualTo(900L)
    }

    "combine charges for multiple coffees and decrement Credit Card balance once for N coffees" in {
      val cafe = new Cafe()
      val cc1 = CreditCard(1234567812345678L, 111L, 1000L)
      val (coffees, charge) = cafe.buyCoffees(cc1, 5)
      val cc2 = Payments().process(cc1, charge)

      cc2.balance must beEqualTo(500L)
    }
  }

}
