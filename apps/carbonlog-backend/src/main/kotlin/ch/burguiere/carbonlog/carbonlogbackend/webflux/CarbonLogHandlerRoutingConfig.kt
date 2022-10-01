package ch.burguiere.carbonlog.carbonlogbackend.webflux

import ch.burguiere.carbonlog.base.CarbonMeasurement
import ch.burguiere.carbonlog.carbonlogbackend.repository.CarbonLogRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.kotlin.core.publisher.toMono
import java.time.Instant
import javax.management.loading.ClassLoaderRepository

@Configuration
class CarbonLogHandlerRoutingConfig(private val carbonLogRepository: CarbonLogRepository) {
    @Bean
    fun configureRouting(): RouterFunction<ServerResponse> = router {
        GET("/carbon-logs/measurements") {
            carbonLogRepository.getMeasurements().collectList()
                .flatMap { ServerResponse.ok().body(BodyInserters.fromValue(it)) }
        }
    }
}
