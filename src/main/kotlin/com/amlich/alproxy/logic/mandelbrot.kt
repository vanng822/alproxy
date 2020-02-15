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

data class InteratorResult(val i:Int, val newline: Boolean)

fun createJob(x: Double, y: Double, newline: Boolean): Deferred<InteratorResult> {
    return GlobalScope.async<InteratorResult> {
        val i = iternator(x, y)
        InteratorResult(i, newline)
    }
}

suspend fun generateMandelbrot(): String {
    var jobs = mutableListOf<Deferred<InteratorResult>>()
    for (y in -39 until 39) {
        var newline = true
        for (x in -39 until 39) {
            val job = createJob(x.toDouble()/40.0, y.toDouble()/40.0, newline)
            jobs.add(job)
            newline = false
        }
    }
    val results = jobs.awaitAll()

    var output = "<pre>"
    results.forEach{
        if (it.newline) {
            output += "\n"
        }
        if (it.i == 0) {
            output += "*"
        } else {
            output += " "
        }
    }
    output += "</pre>"
    return output
}