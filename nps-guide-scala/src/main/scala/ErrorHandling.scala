case class Customer(age: Int)
class Cigarettes
case class UnderAgeException(message: String) extends Exception(message)

object ErrorHandling {

  def buyCigarettes(customer: Customer): Cigarettes =
    if (customer.age < 16) {
      throw UnderAgeException(s"Customer must be older than 16 but was ${customer.age}")
    } else {
      new Cigarettes
    }

}
