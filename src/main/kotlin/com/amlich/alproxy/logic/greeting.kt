package com.amlich.alproxy.logic

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.runBlocking

sealed class CounterMsg
object IncCounter : CounterMsg() // one-way message to increment counter
class GetCounter(val response: CompletableDeferred<Long>) : CounterMsg() // a request with reply

fun CoroutineScope.counterActor() = actor<CounterMsg> {
    var counter:Long = 0 // actor state
    for (msg in channel) { // iterate over incoming messages
        when (msg) {
            is IncCounter -> counter++
            is GetCounter -> msg.response.complete(counter)
        }
    }
}

suspend fun incrementAndGet(counter: SendChannel<CounterMsg>): Long {
    return runBlocking <Long> {
        counter.send(IncCounter)
        // send a message to get a counter value from an actor
        val response = CompletableDeferred<Long>()
        counter.send(GetCounter(response))
        val result = response.await()
        counter.close()
        result
    }
}