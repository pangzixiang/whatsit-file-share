package com.pangzixiang.whatsit.whatsitfileshare.vertx.dto

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import java.time.LocalDateTime

class ControllerRequestCodec(val clazz: Class<ControllerRequest>): MessageCodec<ControllerRequest, ControllerRequest> {

    override fun encodeToWire(buffer: Buffer?, s: ControllerRequest?) {
        throw UnsupportedOperationException()
    }

    override fun decodeFromWire(pos: Int, buffer: Buffer?): ControllerRequest {
        throw UnsupportedOperationException()
    }

    override fun name(): String {
        return "ControllerRequestCodec${LocalDateTime.now()}"
    }

    override fun systemCodecID(): Byte {
        return -1
    }

    override fun transform(s: ControllerRequest): ControllerRequest {
        return s
    }
}
