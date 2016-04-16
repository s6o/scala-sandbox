import concurrent.Future
import concurrent.Promise

case class TaxCut(reduction: Int)

object Politics {

  // Promise creation example 1
  val taxcut1 = Promise[TaxCut]()

  // Promise creation example 2
  val taxcut2: Promise[TaxCut] = Promise()

  val taxcutF1: Future[TaxCut] = taxcut1.future

  // to successfully deliver on a Promise
  taxcut1.success(TaxCut(20))

}
