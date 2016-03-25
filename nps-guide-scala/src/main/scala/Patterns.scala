object Patterns {

  def gameResults(): Seq[(String, Int)] =
    ("Daniel", 3500) :: ("Melissa", 13000) :: ("John", 7000) :: Nil

  def hallOfFame: Seq[String] = for {
    result <- gameResults()
    (name, score) = result
    if (score > 5000)
  } yield name

  def hallOfFame2: Seq[String] = for {
    (name, score) <- gameResults()
    if (score > 5000)
  } yield name

  def wordsWithoutOutliers(wordFrequencies: Seq[(String, Int)]): Seq[String] = {
    wordFrequencies.filter(wf => wf._2 > 3 && wf._2 < 25).map(_._1)
  }

  def wordsWithoutOutliers2(wordFrequencies: Seq[(String, Int)]): Seq[String] = {
    wordFrequencies.filter {
      case (_, f) => f > 3 && f < 25
    } map {
      case (w, _) => w
    }
  }

  def pf: PartialFunction[(String, Int), String] = {
    case (word, freq) if freq > 3 && freq < 25 => word
  }

  def wordsWithoutOutliers3(wordFrequencies: Seq[(String, Int)]): Seq[String] =
    wordFrequencies.collect(pf)

  def wordsWithoutOutliers4(wordFrequencies: Seq[(String, Int)]): Seq[String] =
    wordFrequencies.collect {
      case (word, freq) if freq > 3 && freq < 25 => word
    }

}
