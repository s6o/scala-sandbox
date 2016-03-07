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

  "FPList#setHead" should {
    "return FPList(head) for an empty list" in {
      val l = FPNil
      val expected = FPList[Int](8)
      val result = FPList.setHead(l, 8)
      result must beEqualTo(expected)
    }

    "return FPList(new_head, tail) for a non-empty list" in {
      val l = FPList[Int](8, 6, 4, 2)
      val expected = FPList[Int](10, 6, 4, 2)
      val result = FPList.setHead(l, 10)
      result must beEqualTo(expected)
    }
  }

  "FPList#drop" should {
    "throw an IllegalArgumentException for n < 0" in {
      FPList.drop(FPNil, -1) must throwA[IllegalArgumentException]
    }

    "return None for an empty list or list.size < n" in {
      val l = FPNil
      FPList.drop(l, 1) must beEqualTo(None)
    }

    "return a Option[FPList[A]] with n elements removed from head" in {
      val l = FPList[Int](1, 3, 4, 6, 8)
      val expectedEven = FPList[Int](4, 6, 8)
      FPList.drop(l, 2) must beEqualTo(Some(expectedEven))
    }
  }

}
