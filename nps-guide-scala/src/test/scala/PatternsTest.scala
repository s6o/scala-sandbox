import org.specs2.mutable._

class PatternsTest extends Specification {

  val wf = List(
    ("habitual", 6),
    ("and", 56),
    ("consuetudinary", 2),
    ("additionally", 27),
    ("homely", 5),
    ("society", 13)
  )

  val expectedResult: List[String] = List("habitual", "homely", "society")

  "Patterns#wordsWithoutOutliers" should {
    "return a list" in {
      val result1 = Patterns.wordsWithoutOutliers(wf)
      result1 must beEqualTo(expectedResult)

      val result2 = Patterns.wordsWithoutOutliers2(wf)
      result2 must beEqualTo(expectedResult)

      val result3 = Patterns.wordsWithoutOutliers3(wf)
      result3 must beEqualTo(expectedResult)

      val result4 = Patterns.wordsWithoutOutliers4(wf)
      result4 must beEqualTo(expectedResult)
    }
  }

}

