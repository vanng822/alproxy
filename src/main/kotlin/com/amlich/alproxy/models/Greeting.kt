package com.amlich.alproxy.models

data class Greeting(val id: Long, val name: String) {
     val say: String
        get() = "Hello $name"
}