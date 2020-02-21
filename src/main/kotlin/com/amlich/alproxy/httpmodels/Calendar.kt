package com.amlich.alproxy.httpmodels

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson


data class CalendarResult(val result: Calendar)

data class Calendar(val id: Int, val ownerId: Int, val name: String, val description: String, val privacy: Int) {

    class Deserializer : ResponseDeserializable<Calendar> {
        override fun deserialize(content: String) = Gson().fromJson(content, CalendarResult::class.java).result
    }
}

data class CalendarAndEvents(val calendar: Calendar, val events: Array<Event>?, val subscription: CalendarSubscribe?)

data class CalendarSubscribeResult(val result: CalendarSubscribe)

data class CalendarSubscribe(val canSubsribe: Boolean, val isSubscribed: Boolean) {

    class Deserializer : ResponseDeserializable<CalendarSubscribe> {
        override fun deserialize(content: String) = Gson().fromJson(content, CalendarSubscribeResult::class.java).result
    }
}