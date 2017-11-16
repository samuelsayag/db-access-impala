package com.ss.dao.impala

import java.sql.{Connection, ResultSet, Statement}
import javax.sql.DataSource

import org.slf4j.LoggerFactory

import scala.util.{Failure, Try}

trait ImpalaCoreApi {

  private val logger = LoggerFactory.getLogger(getClass)

  val ds: DataSource

  def performOnPool[Out](ds: DataSource,
                         query: String,
                         f: SmtMap[Out]): Try[Out] =
    Try {
      perfomOnConnection(ds.getConnection(), query, f)
    }.flatten


  def perfomOnConnection[Out](con: Connection,
                              query: String,
                              f: SmtMap[Out]): Try[Out] = {
    def inTry = {
      var stmt: Statement = null
      try {
        stmt = con.createStatement()
        f(stmt, query)
      } finally closeAndLog(stmt, "Statement")
    }

    Try(inTry)
  }

  def genResultSetMapper[Out](mapping: ResultSet2T[Out]): SmtMap[List[Out]] = {

    def rsMap(stmt: Statement, query: String) = {
      var rs: ResultSet = null
      try {
        var res = List.empty[Out]
        rs = stmt.executeQuery(query)

        while (rs.next())
          res = mapping(rs) +: res
        res
      } finally closeAndLog(rs, "ResultSet")
    }

    rsMap
  }


  def genDDLMapper[Out](mapping: Int2T[Out]): SmtMap[Out] =
    (stmt: Statement, query: String) =>
      mapping(stmt.executeUpdate(query))


  def closeAndLog[ToClose <: AutoCloseable](o: ToClose,
                                            hint: String): Unit =
    logFailure(Try(o.close()), hint)


  def logFailure[T](myTry: Try[T], hint: String): Unit = {
    myTry match {
      case Failure(content) =>
        logger.debug(s"While closing $hint, error: $content")
      case _ => ()
    }
  }
}
