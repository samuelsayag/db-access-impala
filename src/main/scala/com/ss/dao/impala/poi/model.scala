package com.ss.dao.impala.poi

case class RestaurantPoivreRouge(resto_id: Long,
                                 address: Option[String],
                                 address2: Option[String],
                                 zipcode: Option[Long],
                                 city: Option[String],
                                 website_url: Option[String],
                                 lat: Option[Double],
                                 long: Option[Double],
                                 resa: Option[Long], // reservation
                                 clc: Option[Long], // clique and collect
                                 drv: Option[Long], // drive
                                 pco: Option[Long],// pre-commande
                                 new_resto: Option[Long],
                                 url_img_popup_resa_workstation: Option[String],
                                 url_img_popup_resa_mobile: Option[String],
                                 url_img_popup_cmd_workstation: Option[String],
                                 url_img_popup_cmd_mobile: Option[String],
                                 url_img_popup_decouv_workstation: Option[String],
                                 url_img_popup_decouv_mobile: Option[String]
                                )

case class ClosestPOI(resto_id: Long,
                      dist: Option[Double],
                      new_resto: Option[Long],
                      ip: String) // distance to the reference point