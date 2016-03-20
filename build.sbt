name := "Top Level Sandbox [choose a project]"

lazy val commonSettings = Seq(
  organization := "s6o.github.com",
  version := "0.0.1",
  scalaVersion := "2.11.7",

  scalacOptions ++= Seq("-feature", "-language:_", "-unchecked", "-deprecation", "-encoding", "utf8"),
  scalacOptions in Test ++= Seq("-Yrangepos"),

  fork in run := true
)

lazy val sandbox = (project in file("sandbox")).
  settings(commonSettings: _*).
  settings(
    name := "Sandbox",
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-core" % "3.7" % "test"
    )
  )

lazy val sentiment_analyzer = (project in file("sentiment-analyzer")).
  settings(commonSettings: _*).
  settings(
    name := "sentiment-analyzer",
    description := "A demo app to showcase sentiment analysis using Standford CoreNLP and Scala",

    libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.5.2" artifacts (Artifact("stanford-corenlp", "models"), Artifact("stanford-corenlp")),
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
  )

lazy val akka_word_counter = (project in file("akka-word-counter")).
  settings(commonSettings: _*).
  settings(
    name := "akka-word-counter",
    description := "A demo app to count number of words in a file using Akka Actors",

    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.1"
  )

lazy val fp_in_scala = (project in file("fp-in-scala")).
  settings(commonSettings: _*).
  settings(
    name := "fp-in-scala",
    description := "Functional Programming in Scala",
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-core" % "3.7" % "test"
    )
  )

lazy val spray_can_http = (project in file("spray-can-http")).
  settings(commonSettings: _*).
  settings(
    name := "spray-can-http",
    description := "Simple HTTP server example from Spray examples",
    libraryDependencies ++= {
      val akkaV = "2.4.2"
      val sprayV = "1.3.3"
      Seq(
        "io.spray"            %%  "spray-can"     % sprayV,
        "io.spray"            %%  "spray-routing" % sprayV,
        "com.typesafe.akka"   %%  "akka-actor"    % akkaV
      )
    }
  )

lazy val nps_guide_scala = (project in file("nps-guide-scala")).
  settings(commonSettings: _*).
  settings(
    name := "nps-guide-scala",
    description := "Examples/exercises from The Neophyte's Guide to Scala",
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2-core" % "3.7" % "test"
    )
  )

