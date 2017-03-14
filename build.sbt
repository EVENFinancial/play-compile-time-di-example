name := "play-compile-time-di-example"
version := "0.0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

enablePlugins(PlayScala)
