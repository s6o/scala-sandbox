object SimpleProgram {

  def abs(n: Int): Int = if (n < 0) -n else n

  def factorial(n: Int): Int = {
    @annotation.tailrec
    def go(n: Int, acc: Int): Int = if (n <= 0) acc else go(n-1, n*acc)
    go(n, 1)
  }

  def fibonacci(n: Int): Int = {
    n match {
      case nth if nth <= 0 => 0
      case nth if nth < 3 => 1
      case _ =>
        @annotation.tailrec
        def adder(a: Int, b: Int, idx: Int): Int = if (idx < n) adder(b, a + b, idx + 1 ) else b
        adder(1, 2, 3)
    }
  }

  private def formatAbs(x: Int): String = {
    val msg = "The absolute value of %d is %d"
    msg.format(x, abs(x))
  }

  def main(args: Array[String]): Unit = {
    println(formatAbs(-42))
  }

}
