package com.ss.dao.impala.poi

import java.sql.ResultSet

import com.ss.dao.impala.{ImpalaCoreApi, ParameterHelper, QueryResultHelper}

import scala.util.Try

trait GreenHornetApi
  extends ImpalaCoreApi
    with MappingGreenHornet
    with QueryResultHelper
    with ParameterHelper {

  def selectPoiAsOption(id: Long): Option[RestaurantPoivreRouge] =
    asOptionWithLog(selectPoiAsTry(id))

  def selectPoiAsTry(id: Long): Try[RestaurantPoivreRouge] = {
    val q =
      s"""select *
         |from green_hornet_k.tdi_restaurant
         |where resto_id = $id""".stripMargin

    checkOne(
      performOnPool(
        ds, q, genResultSetMapper(
          (rs: ResultSet) => map2Restaurant(rs))))
  }

  def selectClosestPOIAsOption(id: String): Option[ClosestPOI] =
    asOptionWithLog(selectClosestPOIAsTry(id))

  def selectClosestPOIAsTry(ip: String): Try[ClosestPOI] = {
    val q =
      s"""select r.resto_id, ipr.dist, '$ip' as ip, r.new_resto
         |from green_hornet_k.ip_restaurant ipr join
         |green_hornet_k.tdi_restaurant r on
         |ipr.resto_id = r.resto_id
         |where ${IPv42Long(ip)} between ipr.ip_start_num and ipr.ip_end_num
       """.stripMargin

    checkOne(
      performOnPool(
        ds, q,
        genResultSetMapper((rs: ResultSet) => mapToClosestPOI(rs))))
  }

  def selectClosestPOIsAsOption(ips: Seq[String]): Option[Seq[ClosestPOI]] =
    asOptionWithLog(selectClosestPOIsAsTry(ips))

  def selectClosestPOIsAsTry(ips: Seq[String]): Try[Seq[ClosestPOI]] = {

    val tmpIp = ips.
      map(ip => s"select ${IPv42Long(ip)} as ipl, '$ip' as ip ").
      mkString(" union all ")

    val q =
      s"""select r.resto_id, ipr.dist, ip, r.new_resto
         |from
         |green_hornet_k.ip_restaurant ipr join
         |green_hornet_k.tdi_restaurant r on
         |  ipr.resto_id = r.resto_id
         |	join ( $tmpIp ) b
         |on ipr.ip_start_num <= b.ipl and ipr.ip_end_num >= b.ipl
       """.stripMargin

    performOnPool(ds, q,
      genResultSetMapper((rs: ResultSet) => mapToClosestPOI(rs)))
  }

}
