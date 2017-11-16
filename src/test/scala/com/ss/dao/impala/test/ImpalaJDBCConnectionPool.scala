package com.ss.dao.impala.test

import java.sql.{Connection, ResultSet, Statement}
import java.util.Properties
import javax.sql.DataSource

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.slf4j.LoggerFactory

import scala.io.Source
import scala.util.{Failure, Try}

object ImpalaJDBCConnectionPool {
  val logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {

    ///;AuthMech=1;principal=benjamin@AFFINYTIX.COM;KrbRealm=AFFINYTIX.COM;KrbHostFQDN=c2-n5.affinytix.com;KrbServiceName=impala"

    val props = new Properties()

    val is = getClass.getClassLoader.getResourceAsStream("hikari.properties")
    Source.fromInputStream(is).getLines().foreach(println)

    Source.fromInputStream(
      getClass.getClassLoader.getResourceAsStream("hikari.properties")
    ).getLines().foreach(s => {
      val keyValueArray: Array[String] = s.split("=", 2)
      println(keyValueArray)
      props.setProperty(keyValueArray(0), keyValueArray(1))
    })

    println(s"In props: ${props.getProperty("jdbcUrl")}")
    println(s"In props: ${props.getProperty("driverClassName")}")

    //print(s"FILE FOUND -------------- $hikariPropFile")
    //val hikariPropFile = args(0)
    val query = "select id, name  from default.page_type"
    val f = (rs: ResultSet) => (rs.getLong("id"), rs.getString("name"))

    val pool = createPool(props)

    val res = performOnPool(pool, query, f)
    res foreach {
      s => s foreach (x => logger.debug(s"[id: ${x._1}, name: ${x._2}]"))
    }

    pool.close()
  }

  def createPool(properties: Properties) = new HikariDataSource(
    new HikariConfig(properties)
  )

  def performOnPool[T](ds: DataSource, query: String, f: ResultSet => T): Try[Seq[T]] =
    Try {
      perfomOnConnection(ds.getConnection(), query, f)
    }.flatMap(x => x)


  def perfomOnConnection[T](con: Connection, query: String, f: ResultSet => T): Try[Seq[T]] = {
    def inTry = {
      var stmt: Statement = null
      try {
        stmt = con.createStatement()
        performOnResultSet(stmt, query, f)
      } finally closeAndLog(stmt, "Statement")
    }

    Try(inTry).flatMap(x => x)
  }

  def performOnResultSet[T](stmt: Statement, query: String, f: ResultSet => T): Try[Seq[T]] = {

    def inTry = {
      var rs: ResultSet = null
      try {
        var res = List.empty[T]
        rs = stmt.executeQuery(query)

        while (rs.next())
          res = f(rs) +: res
        res
      } finally closeAndLog(rs, "ResultSet")
    }

    Try(inTry)
  }

  def closeAndLog[ToClose <: AutoCloseable](o: ToClose, hint: String) =
    logFailure(Try(o.close()), hint)

  def logFailure[T](myTry: Try[T], hint: String): Unit = {
    myTry match {
      case Failure(content) =>
        logger.debug(s"While closing $hint, error: $content")
      case _ => ()
    }
  }
}
