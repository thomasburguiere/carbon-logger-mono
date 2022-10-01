package ch.burguiere.carbonlog.carbonlogbackend.webflux

import ch.burguiere.carbonlog.base.CarbonMeasurement
import ch.burguiere.carbonlog.carbonlogbackend.repository.CarbonLogRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.time.Instant

@Configuration
class CarbonLogHandlerRoutingConfig(private val carbonLogRepository: CarbonLogRepository) {
    @Bean
    fun configureRouting(): RouterFunction<ServerResponse> = router {
        GET("/carbon-logs/measurements") {
            carbonLogRepository.getMeasurements().collectList()
                .flatMap { ServerResponse.ok().body(BodyInserters.fromValue(it)) }
        }

        POST("/carbon-logs/measurements") { request ->
            request.bodyToMono(CarbonMeasurement::class.java)
                .flatMap { carbonLogRepository.insertMeasurement(it) }
                .flatMap { ServerResponse.ok().build() }
        }
        POST("/carbon-logs/measurements/{co2Kg}") { request ->
            val co2Kg = request.pathVariable("co2Kg").toDouble()
            carbonLogRepository.insertMeasurement(CarbonMeasurement(co2Kg, Instant.now()))
                .flatMap { ServerResponse.ok().build() }
        }
    }
}
