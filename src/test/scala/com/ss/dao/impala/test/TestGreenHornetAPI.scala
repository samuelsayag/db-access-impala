package com.ss.dao.impala.test

import java.util.Properties
import javax.sql.DataSource

import com.ss.dao.impala.poi.GreenHornetApi
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.slf4j.LoggerFactory

import scala.io.Source

object TestGreenHornetAPI {

  private final val logger = LoggerFactory.getLogger(getClass)

  class myApi(val ds: DataSource) extends GreenHornetApi

  def main(args: Array[String]): Unit = {

    val pool = createPool

    val api = new myApi(pool)

//    val resPOIasTry = api.selectPoiAsTry(7940)
//    logger.info(s"$resPOIasTry")
//
//    val resPOIasOption = api.selectPoiAsTry(7940)
//    logger.info(s"$resPOIasOption")

      val resClosestPOIasOption = api.selectClosestPOIAsOption("2.1.31.220")
    logger.info(s"$resClosestPOIasOption")

    val resClosestPOIsasOption = api.selectClosestPOIsAsOption(Seq("2.1.31.220", "2.1.32.220"))
    logger.info(s"$resClosestPOIsasOption")

    pool.close()
  }

  def createPool = {

    val props = new Properties()

    Source.fromInputStream(
      getClass.getClassLoader.getResourceAsStream("hikari.properties")
    ).getLines().foreach(s => {
      val keyValueArray: Array[String] = s.split("=", 2)
      props.setProperty(keyValueArray(0), keyValueArray(1))
    })

    logger.info(s"In props: ${props.getProperty("jdbcUrl")}")
    logger.info(s"In props: ${props.getProperty("driverClassName")}")

    new HikariDataSource(
      new HikariConfig(props)
    )
  }
}
