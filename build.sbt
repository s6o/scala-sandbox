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

