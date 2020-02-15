package com.amlich.alproxy.logic

import kotlinx.coroutines.*

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

data class InteratorResult(val results: MutableList<Int>)

suspend fun generateMandelbrot(): String {
    val jobs = mutableListOf<Deferred<InteratorResult>>()
    for (y in -39 until 39) {
        val job = fun(y:Double): Deferred<InteratorResult> {
            return GlobalScope.async<InteratorResult> {
                val results = mutableListOf<Int>()
                for (x in -39 until 39) {
                    results.add(iternator(x.toDouble()/40.0, y/40.0))
                }
                InteratorResult(results)
            }
        }(y.toDouble())

        jobs.add(job)
    }
    val results = jobs.awaitAll()

    var output = "<pre>"
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
    output += "</pre>"
    return output
}