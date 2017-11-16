package com.ss.dao.impala

trait ParameterHelper {

  def IPv42Long(dottedIP: String): Long = {
    val addrArray = dottedIP.split("\\.")

    val power = (i: Int) => 3 - i

    val num = (i: Int) => ((addrArray(i).toInt % 256) * Math.pow(256, power(i))).toLong

    addrArray.indices.map(num).sum
  }

}
