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
    }
}

