package com.amlich.alproxy.logic

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.system.measureTimeMillis

const val BAILOUT = 16
const val MAX_ITERATIONS = 1600

fun iternator(x: Double, y: Double): Int {
    val cr: Double = y - 0.5
    var zi: Double = 0.0
    var zr: Double = 0.0
    var i: Int = 0

    while (true) {
        i++
        val temp: Double = zr * zi
        val zr2: Double = zr * zr
        val zi2: Double = zi * zi
        zr = zr2 - zi2 + cr
        zi = temp + temp + x
        if (zi2 + zr2 > BAILOUT) {
            return i
        }
        if (i > MAX_ITERATIONS) {
            return 0
        }
    }
}

data class IteratorResult(val results: MutableList<Int>)

suspend fun generateMandelbrot(): String {

    var launchResult = ""

    val launchTime = measureTimeMillis {
        launchResult += generateMandelbrotLaunch()
    }

    // traditional way of calling measureTimeMillis
    var asyncResult = ""
    val asyncTime = measureTimeMillis({
        asyncResult += generateMandelbrotAsync()
    })

    return "<div style='text-align: center;'>" +
            "<div>Launch: ${launchTime}</div><pre>${launchResult}</pre></div>" +
            "<div style='text-align: center;'>" +
            "<div>Async: ${asyncTime}</div><pre>${asyncResult}</pre></div>"
}

suspend fun generateMandelbrotAsync(): String {
    val jobs = mutableListOf<Deferred<IteratorResult>>()
    for (y in -39 until 39) {
        val job = fun(y: Double): Deferred<IteratorResult> {
            return GlobalScope.async<IteratorResult> {
                val results = mutableListOf<Int>()
                for (x in -39 until 39) {
                    results.add(iternator(x.toDouble() / 40.0, y / 40.0))
                }
                IteratorResult(results)
            }
        }(y.toDouble())

        jobs.add(job)
    }
    val results = jobs.awaitAll()

    var output = ""
    results.forEach {
        output += "\n"
        it.results.forEach { i ->
            if (i == 0) {
                output += "*"
            } else {
                output += " "
            }
        }
    }
    return output
}

data class IteratorResultLaunch(val results: MutableList<Int>, val index: Int)

suspend fun generateMandelbrotLaunch(): String {
    val c = Channel<IteratorResultLaunch>()
    var index = 0
    for (y in -39 until 39) {
        // weird things, unused t but compile
        // refuse to compile without t
        val t = fun(y: Int, index: Int) {
            GlobalScope.launch {
                val results = mutableListOf<Int>()
                for (x in -39 until 39) {
                    results.add(iternator(x / 40.0, y / 40.0))
                }
                c.send(IteratorResultLaunch(results, index))
            }
        }(y, index)
        index++
    }

    val results = arrayOfNulls<IteratorResultLaunch>(index)

    repeat(index) {
        val result = c.receive()
        results.set(result.index, result)
    }

    c.close()

    var output = ""
    results.forEach {
        output += "\n"
        it!!.results.forEach { i ->
            if (i == 0) {
                output += "*"
            } else {
                output += " "
            }
        }
    }

    return output
}