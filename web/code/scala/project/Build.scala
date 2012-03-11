import sbt._
import Keys._

import PlayProject._

object Resolvers {
// no private nexus for now 

  val res = Seq(
//    kestNexus
  )
}

object BuildSettings {
  import Resolvers._


  val buildScalaVersion = "2.9.1"

  val bsDefaults = Defaults.defaultSettings ++ Seq(
    organization  := "org.kset",
    version       := "0.0.1"
  )
}

object Dependencies {


  //standard
  val commonsIo = "commons-io" % "commons-io" % "2.1"

}

object KsetCollectivePlayBuild extends Build {
  import BuildSettings._
  import Dependencies._

  val depsPlay = Nil

  lazy val play = PlayProject(
    "Play",
    "0.0.1",
    depsPlay,
    mainLang = "SCALA"
  )
 
}
