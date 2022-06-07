package com.pangzixiang.whatsit.whatsitfileshare.vertx

import com.pangzixiang.whatsit.whatsitfileshare.configLoader
import com.pangzixiang.whatsit.whatsitfileshare.ui.common.ApplicationState
import com.pangzixiang.whatsit.whatsitfileshare.utils.Utils.deleteDir
import com.pangzixiang.whatsit.whatsitfileshare.vertx.endpoints.VertxEndpointsRegister
import com.pangzixiang.whatsit.whatsitfileshare.vertx.verticle.FileUploadVerticle
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

object VertxApplication {

    private val logger: Logger = LoggerFactory.getLogger(VertxApplication::class.java)

    fun start(applicationState: ApplicationState) {
        deleteDir(File("./tempFile"))
        val vertxOptions = VertxOptions()
            .setEventLoopPoolSize(configLoader.getInt("vertx.eventLoopPoolSize"))
            .setWorkerPoolSize(configLoader.getInt("vertx.workerPoolSize"))
            .setInternalBlockingPoolSize(configLoader.getInt("vertx.internalBlockingPoolSize"))
        val vertx = Vertx.vertx(vertxOptions)
        vertx.deployVerticle(FileUploadVerticle(applicationState))
        vertx.deployVerticle(VertxEndpointsRegister(applicationState))

        Runtime.getRuntime().addShutdownHook(Thread {
            logger.info("Application shutting down...")
            logger.info("Starting to housekeep...")
            deleteDir(File("./tempFile"))
            deleteDir(File("./outputFile"))
        })
    }
}
