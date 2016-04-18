case class Email(subject: String, text: String, sender: String, recipient: String)

object Freemail {

  type EmailFilter = Email => Boolean
  type SizeChecker = Int => Boolean

  def newMailsForUser(mails: Seq[Email], f: EmailFilter): Seq[Email] = mails.filter(f)
  def complement[A](predicate: A => Boolean): (A => Boolean) = (a: A) => !predicate(a)

  val sentByOneOf: Set[String] => EmailFilter = senders => email => senders.contains(email.sender)
  val notSentByAnyOf: Set[String] => EmailFilter = sentByOneOf.andThen(g => complement(g))
  val sizeConstraint: SizeChecker => EmailFilter = f => email => f(email.text.length)
  val minimumSize: Int => EmailFilter = n => sizeConstraint(_ >= n)
  val maximumSize: Int => EmailFilter = n => sizeConstraint(_ >= n)

  def createSentByOneOfFilter(senders: Set[String]): EmailFilter = email => senders.contains(email.sender)
  def createNotSentByOneOfFilter(senders: Set[String]): EmailFilter = email => senders.contains(email.sender)
  def createSizeConstraintFilter(f: SizeChecker): EmailFilter = email => f(email.text.length)
  def createMinimumSizeFilter(n: Int): EmailFilter = createSizeConstraintFilter(_ >= n)
  def createMaximumSizeFilter(n: Int): EmailFilter = createSizeConstraintFilter(_ <= n)

  def main(args: Array[String]): Unit = {

    val emailFilter: EmailFilter = notSentByAnyOf(Set("johndoe@example.com"))
    val mails = Email(
      subject = "It's me again, your stalker friend!",
      text = "Hello my friend! How are you?",
      sender = "johndoe@example.com",
      recipient = "me@example.com") :: Nil

    val result = newMailsForUser(mails, emailFilter)

    println(result)
  }

}
