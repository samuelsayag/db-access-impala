package com.ss.dao

import java.sql.{ResultSet, Statement}

package object impala {

  type Result[In, Out] = In => Out

  type ResultSet2T[T] = Result[ResultSet, T]

  type Int2T[T] = Result[Int, T]

  type SmtMap[Out] = (Statement, String) => Out

}
