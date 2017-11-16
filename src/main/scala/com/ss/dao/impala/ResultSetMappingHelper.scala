package com.ss.dao.impala

import java.sql.ResultSet

trait ResultSetMappingHelper {


  implicit def get[T](colName: String)
                     (implicit toT: (ResultSet, String) => T, rs: ResultSet): T =
    toT(rs, colName)


  implicit def getOpt[T](colName: String)
                        (implicit toT: (ResultSet, String) => T, rs: ResultSet): Option[T] =
    Option(toT(rs, colName))


  implicit val rsToInt: (ResultSet, String) => Long =
    (rs: ResultSet, colName: String) => rs.getInt(colName)

  implicit val rsToDouble: (ResultSet, String) => Double =
    (rs: ResultSet, colName: String) => rs.getDouble(colName)

  implicit val rsToString: (ResultSet, String) => String =
    (rs: ResultSet, colName: String) => rs.getString(colName)

}
