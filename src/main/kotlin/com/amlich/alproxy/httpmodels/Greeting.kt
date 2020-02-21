package com.amlich.alproxy.httpmodels

data class Greeting(val id: Long, val name: String) {
     val say: String
        get() = "Hello $name"
}