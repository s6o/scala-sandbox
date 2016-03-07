import org.specs2.mutable._

class FPListTest extends Specification {

  "FPList#tail" should {
    "return None for ane empty list" in {
      val l = FPNil
      val result = FPList.tail(l)
      result must beEqualTo(None)
    }

    "return a Some(FPNil) for a FPList[A] of size 1" in {
      val l = FPList[Int](1)
      val result = FPList.tail(l)
      result must beEqualTo(Some(FPNil))
    }

    "return a Some(FPList[A]) for a FPList[A] with size > 1" in {
      val l = FPList[Int](1, 2, 3)
      val result = FPList.tail(l)
      result must beEqualTo(Some(FPList(2, 3)))
    }
  }

}
