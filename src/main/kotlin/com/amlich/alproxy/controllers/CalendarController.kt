package com.amlich.alproxy.controllers

import com.amlich.alproxy.logic.fetchCalendar
import com.amlich.alproxy.logic.fetchCalendarEvents
import com.amlich.alproxy.models.CalendarAndEvents
import kotlinx.coroutines.runBlocking
import java.util.logging.Logger


import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class CalendarController {

    private val logger = Logger.getLogger(CalendarController::class.java.name)

    @GetMapping("/calendar/{calendarId}")
    fun getCalendarAndEvents(@PathVariable(value = "calendarId") calendarId: Int): CalendarAndEvents? {
        var cal = runBlocking {
            fetchCalendar(calendarId)
        }
        return cal
    }

    @GetMapping("/calendar/{calendarId}/events")
    fun getCalendarAndEventsLaunch(@PathVariable(value = "calendarId") calendarId: Int): CalendarAndEvents? {
        var cal = runBlocking {
            fetchCalendarEvents(calendarId)
        }
        return cal
    }
}