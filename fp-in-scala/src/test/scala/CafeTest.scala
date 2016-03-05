import org.specs2.mutable._

class CafeTest extends Specification {

  "Cafe#buyCoffee" should {
    "decrement Credit Card balance once for 1 coffee" in {
      val cafe = new Cafe()
      val cc1 = CreditCard(1234567812345678L, 111L, 1000L)
      val (coffee, charge) = cafe.buyCoffee(cc1)
      val cc2 = Payments.process(charge)

      cc2.balance must beEqualTo(900L)
    }

    "combine charges for multiple coffees and decrement Credit Card balance once for N coffees" in {
      val cafe = new Cafe()
      val cc1 = CreditCard(1234567812345678L, 111L, 1000L)
      val (coffees, charge) = cafe.buyCoffees(cc1, 5)
      val cc2 = Payments.process(charge)

      cc2.balance must beEqualTo(500L)
    }

    "group and combine charges per CreditCard for batch processing" in  {
      val cafe = new Cafe()
      val cc1 = CreditCard(1234567812345678L, 111L, 1000L)
      val cc2 = CreditCard(8976121298712222L, 112L, 1000L)

      val (coffees11, charge11) = cafe.buyCoffee(cc1)
      val (coffees12, charge12) = cafe.buyCoffee(cc1)

      val (coffees21, charge21) = cafe.buyCoffee(cc2)
      val (coffees22, charge22) = cafe.buyCoffee(cc2)
      val (coffees23, charge23) = cafe.buyCoffee(cc2)

      val mixedCharges: List[Charge] = charge23 :: charge11 :: charge21 :: charge12 :: charge21 :: List()
      val cards = Payments.processBatch(mixedCharges).sortBy(_.number)

      cards.head.balance must beEqualTo(800L)
      cards.drop(1).head.balance must beEqualTo(700L)
    }
  }

}
