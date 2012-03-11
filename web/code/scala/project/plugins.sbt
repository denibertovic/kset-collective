// +------------------------------------------------------------------------------------+
// | Play Framework SBT plugin (https://github.com/playframework/Play20)                |   
// | See also: Wiki (https://github.com/playframework/Play20/wiki)                      |   
// +------------------------------------------------------------------------------------+

resolvers ++= Seq(
    DefaultMavenRepository,
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

addSbtPlugin("play" % "sbt-plugin" % "2.0-RC1-SNAPSHOT")
