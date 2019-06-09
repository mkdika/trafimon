package com.github.mkdika.trafimonserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TrafimonServerApplication

fun main(args: Array<String>) {
    runApplication<TrafimonServerApplication>(*args)
}

