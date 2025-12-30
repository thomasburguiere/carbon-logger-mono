package ch.burguiere.carbonlog.carbonlogbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["ch.burguiere.carbonlog"])
open class CarbonlogBackendApplication

fun main(args: Array<String>) {
    runApplication<CarbonlogBackendApplication>(*args)
}
