package com.pangzixiang.whatsit.whatsitfileshare.vertx.endpoints

import com.pangzixiang.whatsit.whatsitfileshare.ui.common.ApplicationState
import com.pangzixiang.whatsit.whatsitfileshare.vertx.BaseVerticle
import com.pangzixiang.whatsit.whatsitfileshare.vertx.annotation.Endpoint
import com.pangzixiang.whatsit.whatsitfileshare.vertx.constant.ContentType
import com.pangzixiang.whatsit.whatsitfileshare.vertx.constant.HttpMethods
import com.pangzixiang.whatsit.whatsitfileshare.vertx.dto.ControllerRequest
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.RoutingContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*
import java.net.URLDecoder


class VertxEndpoints(vertx: Vertx, applicationState: ApplicationState): BaseVerticle(applicationState) {

    init {
        this.vertx = vertx
    }

    private val logger: Logger = LoggerFactory.getLogger(VertxEndpoints::class.java)

    @Endpoint(path = "/health", method = HttpMethods.GET)
    private fun health(req: RoutingContext) {
        req.response()
            .putHeader(ContentType.TYPE.value, ContentType.TEXT.value)
            .end("OK!")
    }

//    @Endpoint(path = "/", method = HttpMethods.GET)
//    private fun index(req: RoutingContext) {
//        val templateEngine: ThymeleafTemplateEngine = ThymeleafTemplateEngine.create(vertx)
//        val data = JsonObject()
//        data.put("appName", configLoader.getString("application.name"))
//        templateEngine.render(data, "templates/index.html")
//        { res ->
//            if (res.succeeded())
//                req.response().end(res.result())
//            else
//                req.fail(res.cause())
//        }
//    }

    @Endpoint(path = "/upload", method = HttpMethods.POST)
    private fun upload(request: RoutingContext) {
        request.request().isExpectMultipart = true
        val req = request.request()
        val fileName = URLDecoder.decode(req.getHeader("fileName"), "utf-8")
        val totalChunk = req.getHeader("totalChunk")
        val chunk = req.getHeader("chunk")
        val filePath = "./tempFile/${fileName}"
        logger.info("received file chunk $chunk, total chunks $totalChunk, file name $fileName")
        req.uploadHandler {fileUpload ->
            val tempFolder = File(filePath)
            if (!tempFolder.exists()) {
                tempFolder.mkdirs()
            }
            fileUpload.streamToFileSystem("${filePath}/" + chunk).onComplete {
                val jsonObject = JsonObject()
                jsonObject.put("fileName", fileName)
                jsonObject.put("totalChunk", totalChunk)
                publish("file-upload-service", ControllerRequest(request, jsonObject))
            }
        }.exceptionHandler {
           logger.error(it.message, it.cause)
        }.end {
            req.response().putHeader(ContentType.TYPE.value, ContentType.JSON.value).end(JsonObject().put("status", 200).encode())
        }

    }
}
