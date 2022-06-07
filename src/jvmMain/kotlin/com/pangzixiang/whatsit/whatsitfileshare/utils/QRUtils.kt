package com.pangzixiang.whatsit.whatsitfileshare.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object QRUtils {
    private var width = 400
    private var height = 400
    private val logger: Logger = LoggerFactory.getLogger(QRUtils::class.java)

    fun generateQR(content: String): ImageBitmap {
        logger.info("generate QR Code for $content")
        val hitMap: HashMap<EncodeHintType, Any> = java.util.HashMap()
        hitMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H)
        hitMap.put(EncodeHintType.CHARACTER_SET, "UTF-8")
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hitMap)
        return MatrixToImageWriter.toBufferedImage(bitMatrix).toComposeImageBitmap()
    }
}
