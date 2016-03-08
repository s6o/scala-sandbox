import org.specs2.mutable._

class FPListTest extends Specification {

  "FPList#append" should {
    "append 2nd FPList[A] to 1st FPList[A]" in {
      FPList.append[Int](FPList(1, 2), FPList(3, 4)) must beEqualTo(FPList[Int](1, 2, 3, 4))
    }
  }

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

  "FPList#dropWhile" should {
    "return FPNil for ane empty list" in {
      FPList.dropWhile[Int](FPNil, n => n % 2 == 0) must beEqualTo(FPNil)
    }

    "return a FPList[A] where the first N elements matching the predicate were dropped" in {
      val nums = FPList[Int](2, 4, 1, 8, 3)
      val expected = FPList[Int](1, 8, 3)
      FPList.dropWhile[Int](nums, n => n % 2 == 0) must beEqualTo(expected)
    }
  }

  "FPList#reverse" should {
    "return FPNil for FPNil" in {
      FPList.reverse(FPNil) must beEqualTo(FPNil)
    }

    "return a reversed list in case of non-empty input list" in {
      FPList.reverse(FPList[Int](1, 2, 3)) must beEqualTo(FPList[Int](3, 2, 1))
    }
  }

  "FPList#filter" should {
    "return FPNil for an empty list" in {
      val l = FPNil
      FPList.filter[Int](l, n => n % 2 == 0) must beEqualTo(FPNil)
    }

    "return a FPList[A] without elements that failed to match predicate f" in {
      val nums = FPList[Int](1, 2, 3, 4, 5, 6, 7, 8)
      val expected = FPList[Int](1, 3, 5, 7)
      val result: FPList[Int] = FPList.filter[Int](nums, n => (n % 2 != 0))
      result must beEqualTo(expected)
    }
  }

}
