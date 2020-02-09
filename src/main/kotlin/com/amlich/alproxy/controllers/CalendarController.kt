package com.amlich.alproxy.controllers

import java.util.logging.Logger;


import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class CalendarController {

    private val logger = Logger.getLogger(CalendarController::class.java.getName())

    @GetMapping("/calendar")
    fun getCalendarAndEvents(@RequestParam(value = "calendarId") iName: Int): Unit {

    }
}