import akka.actor.{Actor, ActorRef, Props, ActorSystem}


case object InitializeFile
case class ProcessLine(string: String)
case class LineWordCount(counter: Integer)


class LineManager extends Actor {

  def receive = {
    case ProcessLine(l) => {
      val wc = l.split(" ").length
      sender ! LineWordCount(wc)
    }
    case _ => println("LineManager: unknown message received")
  }

}


class FileWordCounter(filename: String) extends Actor {

  private var running = false
  private var totalLines = 0
  private var currentLines = 0
  private var result = 0
  private var fileSender: Option[ActorRef] = None

  def receive = {
    case InitializeFile => {
      if (running) {
        println("FileWordCounter: [WARNING] duplicate message received")
      } else {
        running = true
        fileSender = Some(sender)
        import scala.io.Source._
        fromFile(filename).getLines.foreach { line =>
          context.actorOf(Props[LineManager]) ! ProcessLine(line)
          totalLines += 1
        }
      }
    }
    case LineWordCount(wc) => {
      result += wc
      currentLines += 1
      if (currentLines == totalLines) {
        fileSender.map( _ ! result)
      }
    }
    case _ => println("FileWordCounter: unknown message received")
  }

}


object WordCounter {
  
  import akka.util.Timeout
  import scala.concurrent.duration._
  import akka.pattern.ask
  import akka.dispatch.ExecutionContexts._

  implicit val ec = global

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("System")
    val actor = system.actorOf(Props(new FileWordCounter(args(0))))
    implicit val timeout = Timeout(25 seconds)
    val future = actor ? InitializeFile
    future.map { result =>
      println("Total number of words: " + result)
      system.terminate
    }
  }

}

