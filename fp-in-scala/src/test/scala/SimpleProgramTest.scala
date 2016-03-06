import org.specs2.mutable._

class SimpleProgramTest extends Specification {

  "SimpleProgram#fibonacci" should {
    "return 0 if n <= 0" in  {
      SimpleProgram.fibonacci(0) must beEqualTo(0)
    }

    "return 1 if n <= 2" in {
      SimpleProgram.fibonacci(2) must beEqualTo(1)
    }

    "return 2 if n == 3" in {
      SimpleProgram.fibonacci(3) must beEqualTo(2)
    }

    "return 8 if n == 6" in {
      SimpleProgram.fibonacci(6) must beEqualTo(8)
    }

    "return 21 if n == 8" in {
      SimpleProgram.fibonacci(8) must beEqualTo(21)
    }

    "return 233 if n == 13" in {
      SimpleProgram.fibonacci(13) must beEqualTo(233)
    }
  }
}
