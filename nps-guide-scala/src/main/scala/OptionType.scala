case class AUser(id: Int, firstName: String, lastName: String, age: Int, gender: Option[String])

object UserRepository {
  private val users = Map(
    1 -> AUser(1, "John", "Doe", 32, Some("male")),
    2 -> AUser(2, "Johanna", "Doe", 30, None)
  )

  def findById(id: Int): Option[AUser] = users.get(id)

  def findAll = users.values
}

object OptionType {

  def printForename(id: Int): Unit =
    UserRepository.findById(id).foreach(user => println(user.firstName))

  def age(id: Int): Option[Int] = UserRepository.findById(id).map(_.age)

  def gender(id: Int): Option[Option[String]] = UserRepository.findById(id).map(_.gender)

  def gender2(id: Int): Option[String] = UserRepository.findById(id).flatMap(_.gender)

  def filterByAge(id: Int, f: AUser => Boolean): Option[AUser] =
    UserRepository.findById(id).filter(f(_))

}
