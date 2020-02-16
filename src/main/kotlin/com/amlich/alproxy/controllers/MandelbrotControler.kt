package com.amlich.alproxy.controllers


import com.amlich.alproxy.logic.generateMandelbrot
import com.amlich.alproxy.logic.timingStringGeneration
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger


@RestController
class MandelbrotControler {

    private val logger = Logger.getLogger(MandelbrotControler::class.java.name)

    @GetMapping("/mandelbrot")
    fun mandelbrot(): String {
        val mandelbrot = runBlocking {
            // Which keyword before fun can cause that function will be exported
            // all except private???
            val (totalTime, result) = timingStringGeneration {
                generateMandelbrot()
            }
            logger.info("Total time: ${totalTime}")
            result
        }
        return mandelbrot
    }
}