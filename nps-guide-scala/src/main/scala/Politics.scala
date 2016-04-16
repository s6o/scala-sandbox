import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

case class TaxCut(reduction: Int)

object Goverment {

  def redeemCampaignPledge(): Future[TaxCut] = {
    val p = Promise[TaxCut]()
    // This here is to move the computation to another thread i.e. not the caller thread
    // and return immediately the Future object from the Promise p. The caller will receive
    // an answer after ~ 2 seconds
    Future {
      println("Starting the new legislative period.")
      Thread.sleep(2000)
      p.success(TaxCut(20))
      println("We reduced the taxes! You must reelect us!")
    }
    p.future
  }

}

object Politics {

  def main(args: Array[String]): Unit = {
    val tf: Future[TaxCut] = Goverment.redeemCampaignPledge()

    println("Let's see if they remember their promise...")

    tf.onComplete {
      case Success(TaxCut(reduction)) =>
        println(s"Yay! They cut our taxes by $reduction percentage points!")
      case Failure(ex) =>
        println(s"They broke their promises! Because of a ${ex.getMessage}")
    }

    Await.result(tf, 10.seconds)
  }

}
