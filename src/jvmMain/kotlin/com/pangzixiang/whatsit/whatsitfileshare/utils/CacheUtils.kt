package com.pangzixiang.whatsit.whatsitfileshare.utils

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import java.util.concurrent.TimeUnit

object CacheUtils {
    private val cache:Cache<String, Any> = Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).maximumSize(10_000).build()

    fun put(string: String, any: Any) {
        cache.put(string, any)
    }

    fun delete(string: String) {
        cache.invalidate(string)
    }

    fun clear() {
        cache.cleanUp()
    }

    fun get(string: String): Any? {
        return cache.getIfPresent(string)
    }
}
