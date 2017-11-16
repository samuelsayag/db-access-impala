organization := "com.ss"

name := "db-access-impala"

version := "1.1.0-SNAPSHOT"

scalaVersion := "2.11.11"

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
publishTo := Some(Resolver.file("file", new File("/var/lib/jenkins/.m2/repository/")))

resolvers += "Restricted Files" at "http://repo.wepingo.com/repository/"

// For the connexion pool
libraryDependencies += "com.zaxxer" % "HikariCP" % "2.6.3"
// For the Impala Jdbc4.1 Driver
libraryDependencies += "commons-codec" % "commons-codec" % "1.3"
libraryDependencies += "commons-logging" % "commons-logging" % "1.1.1"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.1.3"
libraryDependencies += "org.apache.httpcomponents" % "httpcore" % "4.1.3"
libraryDependencies += "org.apache.thrift" % "libfb303" % "0.9.0"
libraryDependencies += "org.apache.thrift" % "libthrift" % "0.9.0"
libraryDependencies += "log4j" % "log4j" % "1.2.14"
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.5.11"
libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.5.11"
libraryDependencies += "org.apache.zookeeper" % "zookeeper" % "3.4.6"
