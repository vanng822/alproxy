package com.amlich.alproxy.logic


import com.amlich.alproxy.models.*
import kotlinx.coroutines.*

import com.github.kittinunf.fuel.coroutines.awaitObjectResult
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

suspend fun getCalendar(calendarId: Int) : Calendar? {
    try {
        val path = "/api/web/v1/calendar/${calendarId}"
        val req = amlichGet(path, null)
        val res = req.awaitObjectResult(Calendar.Deserializer())
        return res.get()
    } catch (exc: Exception) {
        return null
    }
}

suspend fun getCalendarEvents(calendarId: Int) : Array<Event>? {
    try {
        val path = "/api/web/v1/event"
        val req = amlichGet(path, listOf("calendarId" to calendarId))
        val res = req.awaitObjectResult(CalendarEventDeserializer())
        return res.get()
    } catch (exc: Exception) {
        return null
    }
}

suspend fun getCalendarSubscribe(calendarId: Int): CalendarSubscribe? {
    try {
        val path = "/api/web/v1/calendar/${calendarId}/subscribe"
        val req = amlichGet(path, null)
        val res = req.awaitObjectResult(CalendarSubscribe.Deserializer())
        return res.get()
    } catch (exc: Exception) {
        return null
    }
}

suspend fun fetchCalendar(calendarId: Int): CalendarAndEvents? {
    val cal = GlobalScope.async<Calendar?> {
        getCalendar(calendarId)
    }.await()

    if (cal == null) {
        throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    val events = GlobalScope.async<Array<Event>?> {
        getCalendarEvents(calendarId)
    }
    val subscription = GlobalScope.async<CalendarSubscribe?>{
        getCalendarSubscribe(calendarId)
    }

    return CalendarAndEvents(cal, events.await(), subscription.await())
}