
package com.amlich.alproxy.controllers

import com.amlich.alproxy.logic.fetchCalendar
import com.amlich.alproxy.logic.fetchCalendarEvents
import com.amlich.alproxy.logic.generateMandelbrot
import com.amlich.alproxy.models.CalendarAndEvents
import kotlinx.coroutines.runBlocking
import java.util.logging.Logger


import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class MandelbrotControler {

    private val logger = Logger.getLogger(CalendarController::class.java.name)

    @GetMapping("/mandelbrot")
    fun getCalendarAndEvents(): String {
        var mandelbrot = runBlocking {
            generateMandelbrot()
        }
        return mandelbrot
    }
}