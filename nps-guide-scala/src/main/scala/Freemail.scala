case class Email(subject: String, text: String, sender: String, recipient: String)

object Freemail {

  type EmailFilter = Email => Boolean

  def newMailsForUser(mails: Seq[Email], f: EmailFilter): Seq[Email] = mails.filter(f)

  val sentByOneOf: Set[String] => EmailFilter = senders => email => senders.contains(email.sender)
  val notSentByAnyOf: Set[String] => EmailFilter = senders => email => !senders.contains(email.sender)
  val minimumSize: Int => EmailFilter = n => email => email.text.size >= n
  val maximumSize: Int => EmailFilter = n => email => email.text.size <= n

  def createSentByOneOfFilter(senders: Set[String]): EmailFilter = email => senders.contains(email.sender)
  def createNotSentByOneOfFilter(senders: Set[String]): EmailFilter = email => senders.contains(email.sender)
  def createMinimumSizeFilter(n: Int): EmailFilter = email => email.text.size >= n
  def createMaximumSizeFilter(n: Int): EmailFilter = email => email.text.size <= n

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
