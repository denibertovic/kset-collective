import sbt._
import Keys._

object Resolvers {
// no private nexus for now 

  val res = Seq(
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
    "ESCIDOC" at "https://www.escidoc.org/artifactory/repo"
  )
}

object BuildSettings {
  import Resolvers._


  val buildScalaVersion = "2.9.1"

  val bsDefaults = Defaults.defaultSettings ++ Seq(
    organization  := "org.kset",
    version       := "0.0.1",
    resolvers ++= res

  )

  val bsServer = bsDefaults ++
    Seq(name := "collective-server")

  val bsHarvester = bsDefaults ++
    Seq(name := "collective-harvester")
}

object Dependencies {

  //akka
  val akkaActor = "com.typesafe.akka" % "akka-actor" % "2.0"
  val akkaRemoteActor = "com.typesafe.akka" % "akka-remote" % "2.0"

  //standard
  val commonsIo = "commons-io" % "commons-io" % "2.1"

  //jnotify
  val jnotify = "net.contentobjects.jnotify" % "jnotify" % "0.93"

  //config
  val typesafeconfig = "com.typesafe" % "config" % "0.5.0"

}

object ChimeraBuild extends Build {
  import BuildSettings._
  import Dependencies._

  val depsServer = Seq(
    akkaActor,
    akkaRemoteActor,
    commonsIo
  )

  val depsHarvester = Seq(
    typesafeconfig,
    akkaActor,
    akkaRemoteActor,
    jnotify,
    commonsIo
  )
 

  lazy val server = Project(
    "Server",
    file("server"),
    settings = bsServer ++ Seq(
      libraryDependencies ++= depsServer
    )
  )

  lazy val harvester = Project(
    "Harvester",
    file("harvester"),
    settings = bsHarvester ++ Seq(
      libraryDependencies ++= depsHarvester,
      unmanagedJars in Compile <++= (baseDirectory).map{ bd => (bd / "lib" / System.getProperty("sun.arch.data.model") ** "*").get } 
    )
  )

}
