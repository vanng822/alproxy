package com.amlich.alproxy.controllers


import com.amlich.alproxy.logic.generateMandelbrot
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger


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