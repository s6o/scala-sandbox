object Statistics {
  trait NumberLike[A] {
    def get: A
    def plus(y: NumberLike[A]): NumberLike[A]
    def minus(y: NumberLike[A]): NumberLike[A]
    def divide(y: Int): NumberLike[A]
  }

  case class NumberLikeDouble(x: Double) extends NumberLike[Double] {
    def get: Double = x
    def minus(y: NumberLike[Double]): NumberLike[Double] = NumberLikeDouble(x - y.get)
    def plus(y: NumberLike[Double]): NumberLike[Double] = NumberLikeDouble(x + y.get)
    def divide(y: Int): NumberLike[Double] = NumberLikeDouble(x / y)
  }

  case class NumberLikeInt(x: Int) extends NumberLike[Int] {
    def get: Int = x
    def minus(y: NumberLike[Int]): NumberLike[Int] = NumberLikeInt(x - y.get)
    def plus(y: NumberLike[Int]): NumberLike[Int] = NumberLikeInt(x + y.get)
    def divide(y: Int): NumberLike[Int] = NumberLikeInt(x / y)
  }

  type Quartile[A] = (NumberLike[A], NumberLike[A], NumberLike[A])

  def median[A](xs: Vector[NumberLike[A]]): NumberLike[A] = xs(xs.size / 2)

  def quartiles[A](xs: Vector[NumberLike[A]]): Quartile[A] =
    (xs(xs.size / 4), median(xs), xs(xs.size / 4 * 3))

  def iqr[A](xs: Vector[NumberLike[A]]): NumberLike[A] = quartiles(xs) match {
    case (lowerQuartile, _, upperQuartile) => upperQuartile.minus(lowerQuartile)
  }

  def mean[A](xs: Vector[NumberLike[A]]): NumberLike[A] =
    xs.reduce(_.plus(_)).divide(xs.size)
}
