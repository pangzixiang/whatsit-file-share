package com.pangzixiang.whatsit.whatsitfileshare.utils

import com.pangzixiang.whatsit.whatsitfileshare.vertx.VertxApplication
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.net.Inet4Address
import java.net.NetworkInterface

object Utils {
    private val logger: Logger = LoggerFactory.getLogger(Utils::class.java)

    fun getLocalIpAddress(): String? {
        val networkInterfaces = NetworkInterface.getNetworkInterfaces()
        while (networkInterfaces.hasMoreElements()) {
            val networkInterface = networkInterfaces.nextElement()
            if (networkInterface.isVirtual || ! networkInterface.isUp) {
                continue
            } else {
                val addresses = networkInterface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val ip = addresses.nextElement()
                    if (ip != null && ( ip is Inet4Address)) {
                        if (ip.hostAddress.startsWith("192.168.50")) {
                            logger.info("local IP: ${ip.hostAddress}")
                            return ip.hostAddress
                        }
                    }
                }
            }
        }
        return null
    }

    fun deleteDir(directory: File) {
        val files = directory.listFiles()
        if (files == null) {
            logger.info("[HOUSEKEEP] temp folder NOT FOUND, hence skipping...")
            return
        }
        for (file in files) {
            if (file.isDirectory) {
                deleteDir(file)
            } else {
                file.delete()
                logger.info("[HOUSEKEEP] deleted temp file ${file.name} ...")
            }
        }
        directory.delete()
        logger.info("[HOUSEKEEP] temp folder housekeeping done...")
    }
}
