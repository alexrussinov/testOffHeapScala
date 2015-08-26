name := "testOffHeapScala"

version := "1.0"

scalaVersion := "2.11.7"

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full)

libraryDependencies ++= Seq(
    "sh.den" % "scala-offheap_2.11" % "0.1-SNAPSHOT"
)

enablePlugins(JmhPlugin)