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

inline fun timingStringGenerationPair(block: () -> String ): Pair<Long, String> {
    var result = ""
    val t = measureTimeMillis {
        result += block()
    }
    return Pair(t, result)
}

data class TimingResult(val time: Long, val result: String)

inline fun timingStringGeneration(block: () -> String ): TimingResult {
    var result = ""
    val t = measureTimeMillis {
        result += block()
    }
    return TimingResult(t, result)
}

infix fun TimingResult.minus(other: TimingResult): Long {
    return time - other.time
}

suspend fun generateMandelbrot(): String {

    val launchResult = timingStringGeneration {
        generateMandelbrotLaunch()
    }

    // traditional way of calling timingStringGeneration
    val asyncResult = timingStringGeneration({
        generateMandelbrotAsync()
    })

    return "<div style='text-align: center;'>" +
            "<div>async.time - launch.time = ${asyncResult minus launchResult} </div>" +
            "<div>Launch: ${launchResult.time}</div><pre>${launchResult.result}</pre></div>" +
            "<div style='text-align: center;'>" +
            "<div>Async: ${asyncResult.time}</div><pre>${asyncResult.result}</pre></div>"
}

typealias DeferredIteratorResult = Deferred<IteratorResult>

suspend fun generateMandelbrotAsync(): String {
    val jobs = mutableListOf<DeferredIteratorResult>()
    for (y in -39 until 39) {
        val job = fun(y: Double): DeferredIteratorResult {
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
    val results: List<IteratorResult> = jobs.awaitAll()

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

private suspend fun generateMandelbrotLaunch(): String {
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

    val results: Array<IteratorResultLaunch?> = arrayOfNulls<IteratorResultLaunch>(index)

    repeat(index) {
        val result:IteratorResultLaunch = c.receive()
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