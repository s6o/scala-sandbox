object Sentiment extends Enumeration {
  type Sentiment = Value
  val POSITIVE, NEGATIVE, NETURAL = Value

  def toSentiment(sentiment: Int): Sentiment = sentiment match {
    case x if x == 0 || x == 1 => Sentiment.NEGATIVE
    case 2 => Sentiment.NETURAL
    case x if x == 3 || x == 4 => Sentiment.POSITIVE
  }
}
