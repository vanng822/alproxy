package com.amlich.alproxy.controllers


import com.amlich.alproxy.logic.fetchCalendar
import com.amlich.alproxy.logic.fetchCalendarEvents
import com.amlich.alproxy.httpmodels.CalendarAndEvents
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger


@RestController
class CalendarController {

    private val logger = Logger.getLogger(CalendarController::class.java.name)

    @GetMapping("/calendar/{calendarId}")
    fun getCalendarAndEvents(@PathVariable(value = "calendarId") calendarId: Int): CalendarAndEvents? {
        val cal = runBlocking {
            fetchCalendar(calendarId)
        }
        return cal
    }

    @GetMapping("/calendar/{calendarId}/events")
    fun getCalendarAndEventsLaunch(@PathVariable(value = "calendarId") calendarId: Int): CalendarAndEvents? {
        val cal = runBlocking {
            fetchCalendarEvents(calendarId)
        }
        return cal
    }
}