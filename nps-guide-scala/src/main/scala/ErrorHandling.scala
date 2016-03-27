import scala.io.Source
import scala.util.Try
import java.io.InputStream
import java.net.URL
import java.net.MalformedURLException
import java.io.FileNotFoundException

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

  def stdinUrl(): Try[URL]  =
    Try(parseUrl(scala.io.StdIn.readLine()).getOrElse(new URL("http://duckduck.go")))

  def urlInputStream(url: String): Try[InputStream] = parseUrl(url).flatMap { u =>
    Try(u.openConnection()).flatMap(conn => Try(conn.getInputStream))
  }

  def parseHttpUrl(url: String): Try[URL] = parseUrl(url).filter(_.getProtocol == "http")

  def getUrlContentBook(url: String): Try[Iterator[String]] =
    for {
      url <- parseUrl(url)
      connection <- Try(url.openConnection())
      is <- Try(connection.getInputStream)
      source = Source.fromInputStream(is)
    } yield source.getLines()

  // How could the input stream be closed properly?
  def getUrlContents(url: String): Try[Iterator[String]] =
    for {
      is <- urlInputStream(url)
      source = Source.fromInputStream(is)
    } yield source.getLines()

  def altContents(url: String): Try[Iterator[String]] = getUrlContents(url) recover {
    case e: FileNotFoundException =>
      Iterator("Requested page does not exist")
    case e: MalformedURLException =>
      Iterator("Please make sure to enter a valid URL")
    case _ =>
      Iterator("An unexpected error has occurred")
  }
}
