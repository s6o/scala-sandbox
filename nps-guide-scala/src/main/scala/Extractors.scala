trait User {
  def name: String
  def score: Int

  def greetWithFirstName() = name match {
    case GivenNames(fn, _*) => "Hi " + fn + "!"
    case _ => "Welcome"
  }
}


class FreeUser(val name: String,
               val score: Int,
               val upgradeProbability: Double) extends User
class PremiumUser(val name: String, val score: Int) extends User

object FreeUser {
  def unapply(user: FreeUser): Option[(String, Int, Double)] =
    Some(user.name, user.score, user.upgradeProbability)
}

object PremiumUser {
  def unapply(user: PremiumUser): Option[(String, Int)] =
    Some(user.name, user.score)
}

object GivenNames {
  def unapplySeq(name: String): Option[Seq[String]] = {
    val names = name.trim.split(" ")
    if (names.forall(_.isEmpty)) None else Some(names)
  }
}