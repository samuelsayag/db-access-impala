package com.ss.dao.impala.poi

import java.sql.ResultSet

import com.ss.dao.impala.ResultSetMappingHelper

trait MappingGreenHornet extends ResultSetMappingHelper {

  def map2Restaurant(rs: ResultSet): RestaurantPoivreRouge = {

    implicit val r: ResultSet = rs

    RestaurantPoivreRouge.tupled((
      get("resto_id"),
      getOpt("address"),
      getOpt("address2"),
      getOpt("zipcode"),
      getOpt("city"),
      getOpt("website_url"),
      getOpt("lat"),
      getOpt("long"),
      getOpt("resa"), // reservation
      getOpt("clc"), // clique and collect
      getOpt("drv"), // drive
      getOpt("pco"),
      getOpt("new_resto"),
      getOpt("url_img_popup_resa_workstation"),
      getOpt("url_img_popup_resa_mobile"),
      getOpt("url_img_popup_cmd_workstation"),
      getOpt("url_img_popup_cmd_mobile"),
      getOpt("url_img_popup_decouv_workstation"),
      getOpt("url_img_popup_decouv_mobile")
    ))
  }

  def mapToClosestPOI(rs: ResultSet): ClosestPOI = {

    implicit val r: ResultSet = rs

    ClosestPOI.tupled((
      get("resto_id"),
      getOpt("dist"),
      getOpt("new_resto"),
      get("ip")
    ))
  }


}
