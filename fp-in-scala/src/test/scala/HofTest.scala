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

  "Hof#isSorted" should {
    "return false for an empty array" in {
      val a: Array[Int] = Array.empty[Int]
      val result = Hof.isSorted(a, (i: Int, j: Int) => i < j)
      result must beEqualTo(false)
    }

    "return true if an array's elements are ordered" in {
      val asc: Array[Int] = Array(1, 2, 4, 5)
      val ascResult = Hof.isSorted(asc, (i: Int, j: Int) => i < j)

      val desc: Array[Int] = Array(9, 7, 5, 3, 1)
      val descResult = Hof.isSorted(desc, (i: Int, j: Int) => i > j)

      ascResult must beEqualTo(true)
      descResult must beEqualTo(true)
    }
  }

}
