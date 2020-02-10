package com.amlich.alproxy.logic

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.core.Request

const val AMLICH_BASE_URL = "https://am-lich.com"

suspend fun amlichGet(path: String, params: Parameters?): Request {
    val url: String = "${AMLICH_BASE_URL}${path}"
    return Fuel.get(url, params)
}