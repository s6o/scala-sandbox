name := "scala-sandbox"
version := "0.0.0"

scalaVersion := "2.11.7"
libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.7" % "test")

scalacOptions += "-deprecation"
scalacOptions in Test ++= Seq("-Yrangepos")

