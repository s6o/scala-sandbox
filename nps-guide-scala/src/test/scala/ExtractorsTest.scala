import org.specs2.mutable._

class ExtractorsTest extends Specification {

  "Extractors#PremiumUser" should {
    "provide name in an extractor pattern" in {
      val u = new PremiumUser("King")
      val actualName = u match {
        case FreeUser(name) => name
        case PremiumUser(name) => name
      }
      actualName must beEqualTo("King")
    }
  }

}
