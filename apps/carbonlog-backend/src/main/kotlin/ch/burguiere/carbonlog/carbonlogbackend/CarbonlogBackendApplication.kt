package ch.burguiere.carbonlog.carbonlogbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = arrayOf("ch.burguiere.carbonlog"))
class CarbonlogBackendApplication

fun main(args: Array<String>) {
  runApplication<CarbonlogBackendApplication>(*args)
}


