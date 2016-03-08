object FPTimer {

  sealed trait TimerUnit {
    val name: String
    val unit: String
    val t: () => Long
  }

  case class Nano(name: String) extends TimerUnit {
    val unit = "ns"
    val t = () => System.nanoTime()
  }

  case class Milli(name: String) extends TimerUnit {
    val unit = "ms"
    val t = () => System.currentTimeMillis()
  }

  def measureIn[B](tu: TimerUnit, what: => B): B = {
    val t0 = tu.t()
    val result = what
    val t1 = tu.t()
    println("Elapsed time for [" + tu.name + "] = " + (t1 - t0) + " " + tu.unit)
    result
  }

}
