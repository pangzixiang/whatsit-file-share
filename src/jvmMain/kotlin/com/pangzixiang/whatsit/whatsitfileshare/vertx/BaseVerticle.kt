package com.pangzixiang.whatsit.whatsitfileshare.vertx

import com.pangzixiang.whatsit.whatsitfileshare.vertx.dto.ControllerRequest
import com.pangzixiang.whatsit.whatsitfileshare.vertx.dto.ControllerRequestCodec
import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import io.vertx.core.eventbus.MessageConsumer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class BaseVerticle: AbstractVerticle() {

    private val logger: Logger = LoggerFactory.getLogger(BaseVerticle::class.java)

    fun publish(address: String, request: ControllerRequest) {
        getVertx().eventBus()
            .unregisterDefaultCodec(ControllerRequest::class.java)
            .registerDefaultCodec(ControllerRequest::class.java, ControllerRequestCodec(ControllerRequest::class.java))
            .publish(address, request)
    }

    fun request(address: String, request: ControllerRequest, replyHandler: Handler<AsyncResult<Message<ControllerRequest>>>): EventBus {
        return getVertx().eventBus()
            .unregisterDefaultCodec(ControllerRequest::class.java)
            .registerDefaultCodec(ControllerRequest::class.java, ControllerRequestCodec(ControllerRequest::class.java))
            .request(address, request, replyHandler)
    }

    fun listen(address: String, handler: Handler<Message<ControllerRequest>>){
        getVertx().eventBus()
            .unregisterDefaultCodec(ControllerRequest::class.java)
            .registerDefaultCodec(ControllerRequest::class.java, ControllerRequestCodec(ControllerRequest::class.java))
            .consumer(address, handler).completionHandler { res ->
                if (res.succeeded()) {
                    logger.info("[$address] deployed")
                } else {
                    logger.error("[$address] failed to deploy")
                }
            }
    }

    fun send(address: String, request: ControllerRequest): EventBus {
        return getVertx().eventBus()
            .unregisterDefaultCodec(ControllerRequest::class.java)
            .registerDefaultCodec(ControllerRequest::class.java, ControllerRequestCodec(ControllerRequest::class.java))
            .send(address, request)
    }

}
