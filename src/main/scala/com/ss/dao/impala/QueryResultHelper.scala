package com.ss.dao.impala

import org.slf4j.LoggerFactory

import scala.util.{Failure, Success, Try}

trait QueryResultHelper {

  private val logger = LoggerFactory.getLogger(getClass)

  def checkOne[T](res: Try[Seq[T]],
                  errorMsg: String = "Found more than one result: "): Try[T] = {
    res.flatMap {
      ts =>
        Try {
          if (ts.size > 1)
            throw new Exception(s"$errorMsg [$ts]")
          else ts.head
        }
    }
  }

  def asOption[T](res: Try[T]): Option[T] = res.toOption

  def asOptionWithLog[T](res: Try[T]): Option[T] = {
    res match {
      case Success(_) => res.toOption
      case Failure(e) =>
        logger.error(s"$e")
        res.toOption
    }
  }
}
