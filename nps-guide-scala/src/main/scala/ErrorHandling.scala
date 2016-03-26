import scala.util.Try
import java.net.URL

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

  def cigarettesBuyMessage(customer: Customer): String = {
    try {
      buyCigarettes(customer)
      "Yo, here are your cancer sticks! Happy smokin'!"
    } catch {
      case UnderAgeException(msg) => msg
    }
  }

  def parseUrl(url: String): Try[URL] = Try(new URL(url))

  def stdinUrl(): Try[URL] =
    parseUrl(scala.io.StdIn.readLine()).getOrElse[Try[URL]](parseUrl("http://duckduck.go"))

}
