package com.amlich.alproxy.logic


import com.amlich.alproxy.httpmodels.*
import com.amlich.alproxy.models.CalendarRepository
import kotlinx.coroutines.*

import com.github.kittinunf.fuel.coroutines.awaitObjectResult
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import kotlinx.coroutines.channels.Channel

import com.amlich.alproxy.models.Calendar as DbCalendar

suspend fun getCalendar(calendarId: Int) : Calendar? {
    try {
        val req = Amlich.getCalendar(calendarId)
        val res = req.awaitObjectResult(Calendar.Deserializer())
        return res.get()
    } catch (exc: Exception) {
        return null
    }
}

suspend fun getEvents(calendarId: Int) : Array<Event>? {
    try {
        val req = Amlich.getEvents(calendarId)
        val res = req.awaitObjectResult(CalendarEventDeserializer())
        return res.get()
    } catch (exc: Exception) {
        return null
    }
}

suspend fun getCalendarSubscribe(calendarId: Int): CalendarSubscribe? {
    try {
        val req = Amlich.getCalendarSubscribe(calendarId)
        val res = req.awaitObjectResult(CalendarSubscribe.Deserializer())
        return res.get()
    } catch (exc: Exception) {
        return null
    }
}

suspend fun fetchCalendar(calendarId: Int): CalendarAndEvents? {
    val cal = runBlocking {
        getCalendar(calendarId)
    }

    if (cal == null) {
        throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    val events = GlobalScope.async<Array<Event>?> {
        getEvents(calendarId)
    }
    val subscription = GlobalScope.async<CalendarSubscribe?>{
        getCalendarSubscribe(calendarId)
    }

    return CalendarAndEvents(cal, events.await(), subscription.await())
}

suspend fun getCalendarEvents(calendarId: Int): Array<Event>? {
    try {
        val req = Amlich.getCalendarEvents(calendarId)
        val res = req.awaitObjectResult(CalendarEventDeserializer())
        return res.get()
    } catch (exc: Exception) {
        return null
    }
}

suspend fun fetchCalendarEvents(calendarId: Int): CalendarAndEvents? {
    val cal = runBlocking {
        getCalendar(calendarId)
    }

    if (cal == null) {
        throw ResponseStatusException(HttpStatus.NOT_FOUND)
    }

    val c = Channel<Any?>()

    GlobalScope.launch {
        c.send(getCalendarEvents(calendarId))
    }
    GlobalScope.launch {
        c.send(getCalendarSubscribe(calendarId))
    }

    var events: Array<Event>? = null
    var subscription: CalendarSubscribe? = null

    repeat(2) {
        val res = c.receive()
        when(res) {
            is CalendarSubscribe -> subscription = res
            is Array<*> -> events = res as Array<Event>
        }
    }
    c.close()

    return CalendarAndEvents(cal, events, subscription)
}