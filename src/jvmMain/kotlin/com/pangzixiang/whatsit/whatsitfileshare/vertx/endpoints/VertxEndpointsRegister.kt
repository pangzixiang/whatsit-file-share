package com.pangzixiang.whatsit.whatsitfileshare.vertx.endpoints

import com.pangzixiang.whatsit.whatsitfileshare.configLoader
import com.pangzixiang.whatsit.whatsitfileshare.vertx.BaseVerticle
import com.pangzixiang.whatsit.whatsitfileshare.vertx.annotation.Endpoint
import io.vertx.core.Handler
import io.vertx.core.Promise
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.ext.web.handler.StaticHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.reflect.Method
import kotlin.reflect.KClass

class VertxEndpointsRegister: BaseVerticle() {
    private val logger: Logger = LoggerFactory.getLogger(VertxEndpointsRegister::class.java)

    override fun start(startPromise: Promise<Void>) {
        val router: Router = Router.router(vertx)

        router.route().handler(CorsHandler.create("*"))
//        router.route("/static/*").handler(StaticHandler.create("static"))
        router.route("/*").handler(StaticHandler.create("web-ui"))

        val kClass: KClass<VertxEndpoints> = VertxEndpoints::class
        for (method: Method in kClass.java.declaredMethods) {
            if (method.isAnnotationPresent(Endpoint::class.java)) {
                method.trySetAccessible()
                val endpoint: Endpoint = method.getAnnotation(Endpoint::class.java)
                if (method.returnType.typeName == "void") {
                    router
                        .route(HttpMethod.valueOf(endpoint.method.name), endpoint.path)
                        .handler { req ->
                            method
                                .invoke(kClass.java.getDeclaredConstructor(Vertx::class.java)
                                    .newInstance(vertx), req)
                        }
                } else {
                    router
                        .route(HttpMethod.valueOf(endpoint.method.name), endpoint.path)
                        .handler(
                            method.invoke(kClass.java.getDeclaredConstructor(Vertx::class.java).newInstance(vertx))
                                    as Handler<RoutingContext>?
                        )
                }
                logger.info("endpoint '${endpoint.path}' with method '${endpoint.method.name}' registered [return type: ${method.returnType.typeName}]")
            }
        }

        vertx
            .createHttpServer()
            .requestHandler(router)
            .listen(configLoader.getInt("server.port")) { http ->
                if (http.succeeded()) {
                    startPromise.complete()
                    logger.info("HTTP server started on port '${configLoader.getInt("server.port")}'")
                } else {
                    startPromise.fail(http.cause());
                }
            }
    }
}
