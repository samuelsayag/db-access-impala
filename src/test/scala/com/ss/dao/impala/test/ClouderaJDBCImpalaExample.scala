package com.ss.dao.impala.test

import java.sql.{Connection, DriverManager, ResultSet, Statement}



import org.slf4j.LoggerFactory

import scala.util.{Failure, Try}

object ClouderaJDBCImpalaExample {

  val logger = LoggerFactory.getLogger(this.getClass)

  val JDBCDriver = "com.cloudera.impala.jdbc41.Driver"

  val ConnectionURL = "jdbc:impala://[you server ip or domain]:21050/;AuthMech=1;principal=[princ@REALM];KrbRealm=[REALM];KrbHostFQDN=[domain of one of the impala host];KrbServiceName=impala;LogLevel=6;LogPath=./hikari.log"

  def main(args: Array[String]): Unit = {

    print (ConnectionURL)


    //Class.forName("org.apache.hive.jdbc.HiveDriver")
    Class.forName(JDBCDriver)
    System.out.println("getting connection")
    val con : Connection = DriverManager.getConnection(ConnectionURL)
    System.out.println("got connection")

    val query = "select id, name  from default.page_type"

    val res = simpleSelect(query)

    logger.debug(s"Size of the result: [${res.length}]")
    res foreach (x => logger.debug(s"[id: ${x._1}, name: ${x._2}]"))
    logger.debug("End of the test...")

    con.close()
  }

  def simpleSelect(query: String): List[(Long, String)] = {
    var con: Connection = null
    var stmt: Statement = null
    var rs: ResultSet = null

    var res = List.empty[(Long, String)]

    try {

      con = DriverManager.getConnection(ConnectionURL)
      stmt = con.createStatement()
      rs = stmt.executeQuery(query)

      while (rs.next) {
        res = res :+ (rs.getLong("id"), rs.getString("name"))
      }

    } finally {
      closeAndLog(rs, "ResultSet")
      closeAndLog(stmt, "Statement")
      closeAndLog(con, "Connection")
    }

    res
  }

  def closeAndLog[ToClose <: AutoCloseable](o: ToClose, hint: String) =
    logFailure(Try(o.close()), hint)

  def logFailure[T](myTry: Try[T], hint: String): Unit = {
    myTry match {
      case Failure(content) => logger.debug(s"In the $hint, error: $content")
      case _ => ()
    }
  }
}
