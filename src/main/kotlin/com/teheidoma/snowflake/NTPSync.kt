/*
 * created by teheidoma
 * Copyright (c) 2019.
 */

package com.teheidoma.snowflake

import org.apache.commons.net.ntp.NTPUDPClient
import org.apache.commons.net.ntp.TimeInfo
import org.slf4j.LoggerFactory
import java.net.InetAddress


class NTPSync(private val server:String= "time.google.com"){
    private var timeInfo:TimeInfo?=null
    private var offset: Long?=null
    private val logger = LoggerFactory.getLogger(NTPSync::class.java)

    fun sync(){
        val client = NTPUDPClient()
        client.defaultTimeout = 10_000

        val inetAddress = InetAddress.getByName(server);
        val timeInfo = client.getTime(inetAddress)
        timeInfo.computeDetails()
        if (timeInfo.offset != null) {
            this.timeInfo = timeInfo
            this.offset = timeInfo.offset
        }
        logger.info("current time offset is {} ms", offset!!)
        client.close()
    }


    fun getTime():Long{
        return System.currentTimeMillis()+ this.offset!!
    }
}