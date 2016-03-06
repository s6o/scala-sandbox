object Hof {

  def findFirst[A](a: Array[A], key: A, p: A => Boolean): Int = {
    @annotation.tailrec
    def loop(index: Int): Int = {
      index match {
        case i if i >= a.length => -1
        case i if p(a(i)) => i
        case i => loop(i + 1)
      }
    }
    loop(0)
  }

}
