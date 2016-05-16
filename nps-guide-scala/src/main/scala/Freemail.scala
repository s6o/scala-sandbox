case class Email(subject: String, text: String, sender: String, recipient: String)

object Freemail {

  type EmailFilter = Email => Boolean
  type SizeChecker = Int => Boolean
  type IntPairPred = (Int, Int) => Boolean

  def newMailsForUser(mails: Seq[Email], f: EmailFilter): Seq[Email] = mails.filter(f)
  def complement[A](predicate: A => Boolean): (A => Boolean) = (a: A) => !predicate(a)

  def any[A](predicates: (A => Boolean)*): A => Boolean = (a: A) => predicates.exists(p => p(a))
  def none[A](predicates: (A => Boolean)*): A => Boolean = complement(any(predicates: _*))
  def every[A](predicates: (A => Boolean)*): A => Boolean = none(predicates.view.map(complement(_)): _*)

  def sizeConstraint(pred: IntPairPred, n: Int, email: Email): Boolean = pred(email.text.length, n)

  val gt: IntPairPred = _ > _
  val ge: IntPairPred = _ >= _
  val lt: IntPairPred = _ < _
  val le: IntPairPred = _ <= _
  val eq: IntPairPred = _ == _

  val minimumSize: (Int, Email) => Boolean = sizeConstraint(ge, _: Int, _: Email)
  val maximumSize: (Int, Email) => Boolean = sizeConstraint(le, _: Int, _: Email)

  val constr20: (IntPairPred, Email) => Boolean = sizeConstraint(_: IntPairPred, 20, _: Email)
  val constr30: (IntPairPred, Email) => Boolean = sizeConstraint(_: IntPairPred, 30, _: Email)
  val min20: EmailFilter = minimumSize(20, _: Email)
  val max20: EmailFilter = maximumSize(20, _: Email)

  val sentByOneOf: Set[String] => EmailFilter = senders => email => senders.contains(email.sender)
  val notSentByAnyOf: Set[String] => EmailFilter = sentByOneOf.andThen(g => complement(g))

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
