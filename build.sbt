lazy val server = (project in file("server"))
  .settings(commonSettings)
  .settings(
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    Seq(routesGenerator := InjectedRoutesGenerator),
    pipelineStages := Seq(digest, gzip),
    // triggers scalaJSPipeline when using compile or continuous compilation,
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    libraryDependencies ++= Seq(
      "com.vmunier" %% "scalajs-scripts" % "1.1.4",
      guice,
      specs2 % Test,
      "com.softwaremill.sttp.tapir" %% "tapir-play-server" % "0.17.2",
      "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-play" % "0.17.2",
      "com.softwaremill.sttp.tapir" %% "tapir-redoc-play" % "0.17.2",
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % "0.17.2",
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % "0.17.2"
    )
  )
  .enablePlugins(PlayScala)
  .dependsOn(sharedJvm)

lazy val client = (project in file("client"))
  .settings(commonSettings)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "1.1.0",
     // "com.typesafe.play" %%% "play-json" % "2.9.1",
      "com.softwaremill.sttp.client3" %%% "core" % "3.0.0-RC13",
      "org.lrng.binding" %%% "html" % "latest.release"

    )
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(sharedJs)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
     // "org.julienrf" %%% "play-json-derived-codecs" % "7.0.0",
      "com.typesafe.play" %%% "play-json" % "2.9.1",
      "com.softwaremill.sttp.tapir" %%% "tapir-core" % "0.17.1",
      "com.softwaremill.sttp.tapir" %%% "tapir-json-play" % "0.17.1",
      "com.lihaoyi" %%% "scalatags" % "0.9.2"
  ))
  .jsConfigure(_.enablePlugins(ScalaJSWeb))
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val commonSettings = Seq(
  scalaVersion := "2.13.3",
  organization := "com.nascimento",
  scalacOptions += "-Ymacro-annotations"
)
