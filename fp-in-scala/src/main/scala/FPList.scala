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

  @annotation.tailrec
  def foldLeft[A,B](l: FPList[A], z: B)(f: (B, A) => B): B = l match {
    case FPNil => z
    case Cons(h,t) => foldLeft(t, f(z,h))(f)
  }

  // I failed...
  def foldLeftR[A, B](as: FPList[A], z: B)(f: (B, A) => B): B = ???

  def foldRight[A, B](as: FPList[A], z: B)(f: (A, B) => B): B = as match {
    case FPNil => z
    case Cons(h, t) => f(h, foldRight(t, z)(f))
  }

  def foldRightL[A, B](as: FPList[A], z: B)(f: (A, B) => B): B = foldLeft(reverse(as), z)((b, a) => f(a, b))

  def sum2(ns: FPList[Int]): Int = foldRight(ns, 0)((x, y) => x + y)

  def product2(ns: FPList[Double]): Double = foldRight(ns, 1.0)((x, y) => x * y)

  def length[A](as: FPList[A]): Int = foldRight[A, Int](as, 0)((_, c) => c + 1)

  def sum3(ns: FPList[Int]): Int = foldLeft(ns, 0)((x, y) => x + y)

  def product3(ns: FPList[Double]): Double = foldLeft(ns, 1.0)((x, y) => x * y)

  def length2[A](as: FPList[A]): Int = foldLeft(as, 0)((c, _) => c + 1)

  def apply[A](args: A*): FPList[A] =
    if (args.isEmpty) FPNil else Cons(args.head, apply(args.tail: _*))

  def append[A](a1: FPList[A], a2: FPList[A]): FPList[A] = a1 match {
    case FPNil => a2
    case Cons(h, t) => Cons(h, append(t, a2))
  }

  def tail[A](l: FPList[A]): Option[FPList[A]] = {
    l match {
      case FPNil => None
      case Cons(_, t) => Some(t)
    }
  }

  def setHead[A](l: FPList[A], head: A): FPList[A] = {
    l match {
      case FPNil => FPList(head)
      case Cons(_, t) => Cons(head, t)
    }
  }

  def drop[A](l: FPList[A], n: Int): Option[FPList[A]] = {
    if (n < 0) throw new IllegalArgumentException("Invalid n value, required n > 0")
    l match {
      case FPNil => None
      case Cons(_, t) if n > 0 => drop(t, n - 1)
      case l => Some(l)
    }
  }

  def dropWhile[A](l: FPList[A], f: A => Boolean): FPList[A] = l match {
    case Cons(h, t) if f(h) => dropWhile(t, f)
    case _ => l
  }

  def reverse[A](l: FPList[A]): FPList[A] = {
    @annotation.tailrec
    def loop(res: FPList[A], input: FPList[A]): FPList[A] = input match {
      case Cons(h, t) => loop(Cons(h, res), t)
      case _ => res
    }
    loop(FPNil, l)
  }

  def reverse2[A](as: FPList[A]): FPList[A] = foldRight[A, FPList[A]](as, FPNil)((h, z) => append(z, FPList[A](h)))

  def reverse3[A](as: FPList[A]): FPList[A] = foldLeft[A, FPList[A]](as, FPNil)((z, h) => Cons(h, z))

  def filter[A](l: FPList[A], f: A => Boolean): FPList[A] = {
    @annotation.tailrec
    def collect(resList: FPList[A], inputList: FPList[A], f: A => Boolean): (FPList[A], FPList[A]) = inputList match {
        case Cons(h, t) if f(h) => collect(Cons(h, resList), t, f)
        case Cons(h, t) => collect(resList, t, f)
        case _ => (resList, inputList)
    }
    val (result, _) = collect(FPNil, l, f)
    reverse(result)
  }

  def init[A](l: FPList[A]): FPList[A] = {
    @annotation.tailrec
    def collect(resList: FPList[A], inputList: FPList[A]): (FPList[A], FPList[A]) = inputList match {
      case FPNil => (FPNil, FPNil)
      case Cons(h, FPNil) => (resList, inputList)
      case Cons(h, t) => collect(Cons(h, resList), t)
    }
    val (result, _) = collect(FPNil, l)
    reverse(result)
  }

  def initBookVersion[A](l: FPList[A]): FPList[A] = l match {
    case FPNil => sys.error("init of empty list")
    case Cons(_, FPNil) => FPNil
    case Cons(h, t) => Cons(h, init(t))
  }
}
