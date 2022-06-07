package com.pangzixiang.whatsit.whatsitfileshare.vertx.verticle

import com.pangzixiang.whatsit.whatsitfileshare.ui.common.ApplicationState
import com.pangzixiang.whatsit.whatsitfileshare.vertx.BaseVerticle
import io.vertx.core.Promise
import io.vertx.core.json.JsonObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.*
import java.util.*

class FileUploadVerticle(applicationState: ApplicationState) : BaseVerticle(applicationState) {
    private val logger: Logger = LoggerFactory.getLogger(FileUploadVerticle::class.java)

    override fun start(startPromise: Promise<Void>) {
        listen("file-upload-service") {message ->
            val jsonObject = message.body().data as JsonObject
            val fileName = jsonObject.getString("fileName")
            val filePath = "./tempFile/${fileName}"
            val totalChunk = jsonObject.getString("totalChunk")
            val tempFolder = File(filePath)
            val listFile = tempFolder.listFiles()
            if (listFile != null) {
                if (listFile.size == totalChunk.toInt()) {
                    logger.info("Starting to merge $fileName...")
                    listFile.sort()
                    val vector: Vector<FileInputStream> = Vector<FileInputStream>()
                    for (f in listFile) {
                        vector.add(FileInputStream(f))
                    }
                    val sequenceInputStream = SequenceInputStream(vector.elements())
                    val outputFileDir = File("./outputFile")
                    if (!outputFileDir.exists())
                        outputFileDir.mkdir()
                    val bufferOutputStream = BufferedOutputStream(FileOutputStream(File("./outputFile/${fileName}")))
                    var len: Int
                    val byte = ByteArray(1024*1024)
                    do {
                        len = sequenceInputStream.read(byte)
                        if (len != -1) {
                            bufferOutputStream.write(byte)
                            bufferOutputStream.flush()
                        } else {
                            break
                        }
                    } while (true)
                    bufferOutputStream.close()
                    applicationState.addOutputFileList(File("./outputFile/${fileName}"))
//                    Utils.deleteDir(File(filePath))
                }
            }
        }
    }
}
