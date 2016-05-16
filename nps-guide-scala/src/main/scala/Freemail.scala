import Freemail.EmailFilter

case class Email(subject: String, text: String, sender: String, recipient: String)

case class EmailUser(name: String)

trait EmailRepository {
  def getMails(user: EmailUser, unread: Boolean): Seq[Email]
}

trait FilterRepository {
  def getEmailFilter(user: EmailUser): EmailFilter
}

trait MailboxService {
  def getNewMails(emailRepo: EmailRepository)(filterRepo: FilterRepository)(user: EmailUser): Seq[Email] =
    emailRepo.getMails(user, true).filter(filterRepo.getEmailFilter(user))

  val newMails: EmailUser => Seq[Email]
}

object MockEmailRepository extends EmailRepository {
  def getMails(user: EmailUser, unread: Boolean): Seq[Email] = Nil
}

object MockFilterRepository extends FilterRepository {
  def getEmailFilter(user: EmailUser): EmailFilter = _ => true
}

object MailboxServiceWithMockDeps extends MailboxService {
  val newMails: (EmailUser) => Seq[Email] = getNewMails(MockEmailRepository)(MockFilterRepository) _
}

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

  def sizeConstraintPL(pred: IntPairPred)(n: Int)(email: Email): Boolean = pred(email.text.length, n)
  val sizeConstraintFn: IntPairPred => Int => Email => Boolean = sizeConstraintPL _

  val gt: IntPairPred = _ > _
  val ge: IntPairPred = _ >= _
  val lt: IntPairPred = _ < _
  val le: IntPairPred = _ <= _
  val eq: IntPairPred = _ == _

  val minimumSize: (Int, Email) => Boolean = sizeConstraint(ge, _: Int, _: Email)
  val maximumSize: (Int, Email) => Boolean = sizeConstraint(le, _: Int, _: Email)

  val minSize: Int => Email => Boolean = sizeConstraintPL(ge)
  val maxSize: Int => Email => Boolean = sizeConstraintPL(le)

  val constr20: (IntPairPred, Email) => Boolean = sizeConstraint(_: IntPairPred, 20, _: Email)
  val constr30: (IntPairPred, Email) => Boolean = sizeConstraint(_: IntPairPred, 30, _: Email)
  val min20: EmailFilter = minimumSize(20, _: Email)
  val max20: EmailFilter = maximumSize(20, _: Email)

  val min20c: Email => Boolean = minSize(20)
  val max20c: Email => Boolean = maxSize(20)

  val min20ca: Email => Boolean = sizeConstraintFn(ge)(20)
  val max20ca: Email => Boolean = sizeConstraintFn(le)(20)

  val sum: (Int, Int) => Int = _ + _
  val sumCurried: Int => Int => Int = sum.curried

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
