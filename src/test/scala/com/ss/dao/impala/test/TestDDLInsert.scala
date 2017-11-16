package com.ss.dao.impala.test

import java.sql.ResultSet
import java.util.Properties
import javax.sql.DataSource

import com.ss.dao.impala.ImpalaCoreApi
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.slf4j.LoggerFactory

import scala.io.Source

object TestDDLInsert {
  val logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {

    class CoreAPI(val ds: DataSource) extends ImpalaCoreApi
    val ds: HikariDataSource = createPool(loadProperties("hikari.properties"))
    val api = new CoreAPI(ds)


    val qCreate =
      """create table test_insert (id BIGINT,label STRING , PRIMARY KEY (id) ) PARTITION BY HASH (id) partitions 10 STORED as KUDU""".stripMargin
    val qDrop = """drop table test_insert"""

    def insert(i: Int) = s"insert into test_insert (id, label) values ($i, 'label_$i')"

    val f1 = (rs: ResultSet) => (rs.getLong("id"), rs.getString("label"))
    val f2 = (fetch: Int) => fetch


    // creation of the table
    api.performOnPool(ds, qCreate, api.genDDLMapper(f2))
    // insert of the line
    api.performOnPool(ds, insert(1), api.genDDLMapper(f2))
    // drop of the table
    api.performOnPool(ds, qDrop, api.genDDLMapper(f2))

    ds.close()
  }

  def createPool(properties: Properties) = new HikariDataSource(
    new HikariConfig(properties)
  )

  def loadProperties(filename: String): Properties = {
    val props = new Properties()

    val is = getClass.getClassLoader.getResourceAsStream(filename)
    Source.fromInputStream(is).getLines().foreach(println)

    Source.fromInputStream(
      getClass.getClassLoader.getResourceAsStream(filename)
    ).getLines().foreach(s => {
      val keyValueArray: Array[String] = s.split("=", 2)
      println(keyValueArray)
      props.setProperty(keyValueArray(0), keyValueArray(1))
    })

    println(s"In props: ${props.getProperty("jdbcUrl")}")
    println(s"In props: ${props.getProperty("driverClassName")}")

    props
  }

}
