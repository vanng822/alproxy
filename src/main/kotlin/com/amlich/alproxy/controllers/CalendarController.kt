package com.amlich.alproxy.controllers

import com.amlich.alproxy.logic.fetchCalendar
import com.amlich.alproxy.logic.getCalendar
import com.amlich.alproxy.models.Calendar
import com.amlich.alproxy.models.CalendarAndEvents
import kotlinx.coroutines.runBlocking
import java.util.logging.Logger;


import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class CalendarController {

    private val logger = Logger.getLogger(CalendarController::class.java.getName())

    @GetMapping("/calendar")
    fun getCalendarAndEvents(@RequestParam(value = "calendarId") calendarId: Int): CalendarAndEvents? {
        var cal = runBlocking {
            fetchCalendar(calendarId)
        }
        return cal
    }
}