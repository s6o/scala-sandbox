import org.specs2.mutable._

class HofTest extends Specification {

  "Hof#findFirst" should {
    "return -1 if key was not found" in {
      val a: Array[String] = Array("AB", "BC", "CDEF")
      val result = Hof.findFirst(a, "XW", (item: String) => item == "XW")
      result must beEqualTo(-1)
    }

    "return an index 0..N-1 if key was found" in {
      val a: Array[String] = Array("AB", "BC", "CDEF")
      val result = Hof.findFirst(a, "BC", (item: String) => item == "BC")
      result must beEqualTo(1)
    }
  }

}
