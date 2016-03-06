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

  def isSorted[A](a: Array[A], ordered: (A, A) => Boolean): Boolean = {
    /*
    // looks like an incorrect implementation, tests fail
    // line 137, https://github.com/fpinscala/fpinscala/blob/master/answers/src/main/scala/fpinscala/gettingstarted/GettingStarted.scala
    @annotation.tailrec
    def go(n: Int): Boolean =
      if (n >= a.length-1) true
      else if (ordered(a(n), a(n+1))) false
      else go(n+1)

    go(0)
    */
    @annotation.tailrec
    def loop(index: Int): Boolean = {
      index match {
        case _ if a.isEmpty => false
        case i if i >= a.length - 1 => true
        case i if ordered(a(i), a(i + 1)) => loop(i + 1)
        case _ => false
      }
    }
    loop(0)
  }

  def partial1[A, B, C](a: A, f: (A, B) => C): B => C = {
    (b: B) => f(a, b)
  }

  /** Exercise 2.3. */
  def curry[A, B, C](f: (A, B) => C): A => (B => C) = {
    (a: A) => ((b: B) => f(a, b))
  }

  /** Exercise 2.4. */
  def uncurry[A, B, C](f: A => B => C): (A, B) => C = {
    (a: A, b: B) => f(a)(b)
  }

  /** Exercise 2.5. */
  def compose[A, B, C](f: B => C, g: A => B): A => C = {
    (a: A) => f(g(a))
  }

}
