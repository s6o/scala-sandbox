object Math {
  import annotation.implicitNotFound
  @implicitNotFound("No member of type class NumberLike in scope for ${T}")
  trait NumberLike[T] {
    def plus(x: T, y: T): T
    def divide(x: T, y: Int): T
    def minus(x: T, y: T): T
  }
}

object NumberLike {
  implicit object NumberLikeDouble extends Math.NumberLike[Double] {
    def plus(x: Double, y: Double): Double = x + y
    def divide(x: Double, y: Int): Double = x / y
    def minus(x: Double, y: Double): Double = x - y
  }

  implicit object NumberLikeInt extends Math.NumberLike[Int] {
    def plus(x: Int, y: Int): Int = x + y
    def divide(x: Int, y: Int): Int = x / y
    def minus(x: Int, y: Int): Int = x - y
  }
}

object Statistics {
  import Math.NumberLike

  def mean[T](xs: Vector[T])(implicit ev: NumberLike[T]): T = ev.divide(xs.reduce(ev.plus), xs.size)

  def median[T](xs: Vector[T])(implicit ev: NumberLike[T]): T = xs(xs.size / 2)

  def quartiles[T](xs: Vector[T])(implicit ev: NumberLike[T]): (T, T, T) =
    (xs(xs.size / 4), median[T](xs), xs(xs.size / 4 * 3))

  def iqr[T](xs: Vector[T])(implicit ev: NumberLike[T]): T = quartiles(xs) match {
    case (lowerQuartile, _, upperQuartile) => ev.minus(upperQuartile, lowerQuartile)
  }

  def main(args: Array[String]): Unit = {
    import NumberLike._
    val numbers = Vector[Double](13, 23.0, 42, 45, 61, 73, 96, 100, 199, 420, 900, 3839)
    println(Statistics.mean(numbers))
  }
}
