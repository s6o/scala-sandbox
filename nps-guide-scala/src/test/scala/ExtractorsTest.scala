import org.specs2.mutable._

class ExtractorsTest extends Specification {

  "Extractors#PremiumUser" should {
    "provide name in an extractor pattern" in {
      val u = new FreeUser("King", 3000, 0.75)
      val (actualName, score) = u match {
        case FreeUser(name, s, _) => (name, s)
        case PremiumUser(name, s) => (name, s)
      }
      actualName must beEqualTo("King")
      score must beEqualTo(3000)
    }
  }

  "User#greetWithFirstName" should {
    "create a greeting based on first given name" in {
      val greeting = new PremiumUser("James Doe Smith", 100).greetWithFirstName
      greeting must beEqualTo("Hi James!")
    }
  }

  "Uses#greet" should {
    "create greeting with first and second given names" in {
      val greeting = new PremiumUser("Jane Doe", 100).greet
      greeting must beEqualTo("Hi Jane Doe!")
    }
  }

}
