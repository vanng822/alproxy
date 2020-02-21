package com.amlich.alproxy.httpmodels

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Event(val id: Int, val title: String, val description: String)

data class CalendarEventsResult(val result: Array<Event>)

class CalendarEventDeserializer : ResponseDeserializable<Array<Event>> {
    override fun deserialize(content: String) = Gson().fromJson(content, CalendarEventsResult::class.java).result
}