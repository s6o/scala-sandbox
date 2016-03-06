sealed trait FPList[+A]
case object FPNil extends FPList[Nothing]
case class Cons[+A](head: A, tail: FPList[A]) extends FPList[A]

object FPList {

  def sum(ints: FPList[Int]): Int = ints match {
    case FPNil => 0
    case Cons(h, t) => h + sum(t)
  }

  def product(ds: FPList[Double]): Double = ds match {
    case FPNil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(h, t) => h * product(t)
  }

  def apply[A](args: A*): FPList[A] =
    if (args.isEmpty) FPNil else Cons(args.head, apply(args.tail: _*))

}
