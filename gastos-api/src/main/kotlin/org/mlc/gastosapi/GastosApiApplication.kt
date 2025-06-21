package org.mlc.gastosapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GastosApiApplication

fun main(args: Array<String>) {
    runApplication<GastosApiApplication>(*args)
}
