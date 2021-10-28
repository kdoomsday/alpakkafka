name := "akka-quickstart-scala"

version := "1.0"

scalaVersion := "2.13.5"

lazy val akkaVersion         = "2.6.16"
lazy val AlpakkaVersion      = "2.1.1"
lazy val JacksonVersion      = "2.11.4"
lazy val JacksonModulVersion = "2.12.5"
lazy val slickVersion        = "3.3.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
  "ch.qos.logback"     % "logback-classic"          % "1.2.3",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest"     %% "scalatest"                % "3.1.0"     % Test
)

// Akka Kafka deps
libraryDependencies ++= Seq(
  "com.typesafe.akka"            %% "akka-stream-kafka"    % AlpakkaVersion,
  "com.typesafe.akka"            %% "akka-stream"          % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.2.6",
  "com.fasterxml.jackson.core"    % "jackson-core"         % JacksonVersion,
  "com.fasterxml.jackson.core"    % "jackson-databind"     % JacksonVersion,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % JacksonModulVersion
)

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick"          % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "org.slf4j"           % "slf4j-nop"      % "1.6.4",
  "org.postgresql"      % "postgresql"     % "9.4-1206-jdbc42"
)
