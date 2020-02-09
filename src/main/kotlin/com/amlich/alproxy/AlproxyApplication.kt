package com.amlich.alproxy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AlproxyApplication

fun main(args: Array<String>) {
	runApplication<AlproxyApplication>(*args)
}
