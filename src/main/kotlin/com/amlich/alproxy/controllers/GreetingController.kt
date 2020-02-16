package com.amlich.alproxy.controllers

import com.amlich.alproxy.models.Greeting
import io.vertx.core.Vertx
import io.vertx.redis.RedisClient
import io.vertx.redis.RedisOptions
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger

fun createRedisClient(): RedisClient {
    // Create the redis client
    var client = RedisClient.create(Vertx.vertx(), RedisOptions())
    return client
}

suspend fun doIncr(counter: AtomicLong):Long {
    val no = counter.incrementAndGet()
    return no
}

suspend fun dodirectIncr(counter: AtomicLong):Long {
    return counter.incrementAndGet()
}

suspend fun incrNonBlocking(counter: AtomicLong): Long = coroutineScope {
    val id = async<Long> {
        // launch a new coroutine in background and continue
        doIncr(counter)
    }

    val id2 = async<Unit> {
        // launch a new coroutine in background and continue
        dodirectIncr(counter)
    }

    val r = id.await()
    r
}

@RestController
class GreetingController {

    private val counter = AtomicLong()

    private val logger = Logger.getLogger(GreetingController::class.java.name)

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name") iName: String): Greeting {
        val id = runBlocking<Long> {
            logger.info(iName)
            if (iName == "test") {
                incrNonBlocking(counter)
            } else {
                counter.incrementAndGet()
            }
        }
        return Greeting(id, iName)
    }
}