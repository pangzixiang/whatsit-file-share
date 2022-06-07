package com.pangzixiang.whatsit.whatsitfileshare.vertx.annotation

import com.pangzixiang.whatsit.whatsitfileshare.vertx.constant.HttpMethods

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Endpoint(val path: String, val method: HttpMethods)
