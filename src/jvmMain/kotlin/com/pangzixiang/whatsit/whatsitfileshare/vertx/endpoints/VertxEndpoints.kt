package com.pangzixiang.whatsit.whatsitfileshare.vertx.endpoints

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.pangzixiang.whatsit.whatsitfileshare.ui.common.ApplicationState
import com.pangzixiang.whatsit.whatsitfileshare.utils.CacheUtils
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
            .end(CacheUtils.get("health").toString())
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

    @Endpoint(path = "/download/:fileName", method = HttpMethods.GET)
    private fun download(request: RoutingContext) {
        val fileName = URLDecoder.decode(request.pathParam("fileName"), "utf-8")
        logger.info(fileName)
        val tempList = CacheUtils.get("downloadFileList")
        if (tempList != null) {
            val list = tempList as SnapshotStateList<*>
            list.forEach {
                val file = it as File
                if (file.name.equals(fileName)) {
                    request
                        .response()
                        .putHeader("Content-Disposition", "attachment;filename=${file.name}")
                        .sendFile(file.path)
                    return
                }
            }
        }
        request.response().setStatusCode(404).end("NOT FOUND")
    }

    @Endpoint(path = "/getDownloadList", method = HttpMethods.GET)
    private fun getDownload(request: RoutingContext) {
        val response = JsonObject()
        val tempList = CacheUtils.get("downloadFileList")
        val fileList = ArrayList<JsonObject>()
        if (tempList != null) {
            val list = tempList as SnapshotStateList<*>
            list.forEach {
                val file = it as File
                val temp = JsonObject()
                temp.put("name", file.name)
                temp.put("size", file.length())
                temp.put("lastModified", file.lastModified())
                fileList.add(temp)
            }

        }
        response.put("data", fileList)
        request.response().putHeader(ContentType.TYPE.value, ContentType.JSON.value).end(response.encode())
    }
}
