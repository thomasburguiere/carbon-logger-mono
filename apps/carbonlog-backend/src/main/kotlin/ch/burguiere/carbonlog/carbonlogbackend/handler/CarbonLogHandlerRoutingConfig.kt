package ch.burguiere.carbonlog.carbonlogbackend.handler

import ch.burguiere.carbonlog.base.CarbonMeasurement
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.kotlin.core.publisher.toMono
import java.time.Instant

@Configuration
class CarbonLogHandlerRoutingConfig {
    @Bean
    fun configureRouting(): RouterFunction<ServerResponse> = router {
        GET("/carbon-logs") {
            CarbonMeasurement(0.0, Instant.now()).toMono()
                .flatMap { ServerResponse.ok().body(BodyInserters.fromValue(it)) }
        }
    }
}
