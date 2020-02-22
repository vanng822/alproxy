package com.amlich.alproxy.controllers


import com.amlich.alproxy.httpmodels.Calendar
import com.amlich.alproxy.httpmodels.CalendarAndEvents
import com.amlich.alproxy.logic.fetchCalendar
import com.amlich.alproxy.logic.fetchCalendarEvents
import com.amlich.alproxy.models.CalendarRepository
import com.amlich.alproxy.models.EventRepository
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger
import com.amlich.alproxy.models.Calendar as DbCalendar
import com.amlich.alproxy.models.Event as DbEvent


@RestController
class CalendarController {

    private val logger = Logger.getLogger(CalendarController::class.java.name)


    @Autowired
    var calRepo: CalendarRepository? = null

    @Autowired
    val eventRepo: EventRepository? = null

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
        if (cal != null) {
            val calendar: Calendar = cal.calendar
            calRepo!!.save(DbCalendar(calendar.id, calendar.ownerId, calendar.name, calendar.description, calendar.privacy))
            if (cal.events != null) {
                cal.events.forEach {
                    eventRepo!!.save(DbEvent(it.id, calendar.id, it.title, it.description))
                }
            }
        }
        return cal
    }
}