package com.amlich.alproxy.logic

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.core.Request
import kotlinx.coroutines.withTimeout

class Amlich {

    companion object {
        var AMLICH_HTTP_TIMEOUT_MILLISECS = 1500L

        const val AMLICH_BASE_URL = "https://am-lich.com"

        suspend fun get(path: String, params: Parameters?): Request {
            val url: String = "${AMLICH_BASE_URL}${path}"
            return withTimeout(AMLICH_HTTP_TIMEOUT_MILLISECS) {
                Fuel.get(url, params)
            }

        }

        suspend fun getCalendar(calendarId: Int): Request {
            val path = "/api/web/v1/calendar/${calendarId}"
            return get(path, null)
        }

        suspend fun getEvents(calendarId: Int): Request {
            val path = "/api/web/v1/event"
            return get(path, listOf("calendarId" to calendarId))
        }

        suspend fun getCalendarSubscribe(calendarId: Int): Request {
            val path = "/api/web/v1/calendar/${calendarId}/subscribe"
            return this.get(path, null)
        }

        suspend fun getCalendarEvents(calendarId: Int): Request {
            val path = "/api/web/v1/calendar/${calendarId}/events"
            return this.get(path, null)
        }
    }
}

