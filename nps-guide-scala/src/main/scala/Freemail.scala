case class Email(subject: String, text: String, sender: String, recipient: String)

object Freemail {

  type EmailFilter = Email => Boolean
  type SizeChecker = Int => Boolean

  def newMailsForUser(mails: Seq[Email], f: EmailFilter): Seq[Email] = mails.filter(f)
  def complement[A](predicate: A => Boolean): (A => Boolean) = (a: A) => !predicate(a)

  def any[A](predicates: (A => Boolean)*): A => Boolean = (a: A) => predicates.exists(p => p(a))
  def none[A](predicates: (A => Boolean)*): A => Boolean = complement(any(predicates: _*))
  def every[A](predicates: (A => Boolean)*): A => Boolean = none(predicates.view.map(complement(_)): _*)

  val sentByOneOf: Set[String] => EmailFilter = senders => email => senders.contains(email.sender)
  val notSentByAnyOf: Set[String] => EmailFilter = sentByOneOf.andThen(g => complement(g))
  val sizeConstraint: SizeChecker => EmailFilter = f => email => f(email.text.length)
  val minimumSize: Int => EmailFilter = n => sizeConstraint(_ >= n)
  val maximumSize: Int => EmailFilter = n => sizeConstraint(_ >= n)

  val addMissingSubject: Email => Email =
    (email: Email) => if (email.subject.isEmpty) email.copy(subject = "No subject") else email
  val spellChecking: Email => Email =
    (email: Email) => email.copy(text = email.text.replaceAll("your", "you're"))
  val removeInappropriateLanguage: Email => Email =
    (email: Email) => email.copy(text = email.text.replaceAll("dynamic typing", "**CENSORED**"))
  val addAdvertisementToFooter: Email => Email =
    (email: Email) => email.copy(text = email.text + "\nThis mail set via Super Awesome Free Mail")

  def main(args: Array[String]): Unit = {

    val filter: EmailFilter = every(
      sentByOneOf(Set("johndoe@example.com"))
    )
    val pipeline: Email => Email = Function.chain(Seq(
      addMissingSubject,
      spellChecking,
      removeInappropriateLanguage,
      addAdvertisementToFooter
    ))
    val mails = Email(
      subject = "It's me again, your stalker friend!",
      text = "Hello my friend! How are you?",
      sender = "johndoe@example.com",
      recipient = "me@example.com") :: Nil

    val result = newMailsForUser(mails.map(pipeline(_)), filter)

    println(result)
  }

}
