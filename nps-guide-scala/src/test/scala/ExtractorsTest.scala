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

}
