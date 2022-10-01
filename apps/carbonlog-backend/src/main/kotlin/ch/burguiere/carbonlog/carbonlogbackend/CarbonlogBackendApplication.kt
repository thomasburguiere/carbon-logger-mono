package ch.burguiere.carbonlog.carbonlogbackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = arrayOf("ch.burguiere.carbonlog"), exclude = [MongoAutoConfiguration::class])
class CarbonlogBackendApplication

fun main(args: Array<String>) {
    runApplication<CarbonlogBackendApplication>(*args)
}
