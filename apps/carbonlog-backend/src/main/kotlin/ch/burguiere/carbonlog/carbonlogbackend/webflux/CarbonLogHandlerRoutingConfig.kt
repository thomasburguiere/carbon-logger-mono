package ch.burguiere.carbonlog.carbonlogbackend.webflux

import ch.burguiere.carbonlog.base.CarbonMeasurement
import ch.burguiere.carbonlog.carbonlogbackend.repository.CarbonLogRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.time.Instant

@Configuration
open class CarbonLogHandlerRoutingConfig(
    private val carbonLogRepository: CarbonLogRepository,
    @param:Value("\${static.auth.token}") private val staticToken: String,
) {
    @Bean
    open fun configureRouting(): RouterFunction<ServerResponse> = router {

        GET("/") {
            ServerResponse.ok().bodyValue("CarbonLogBackend is running").toMono()
        }

        GET("/carbon-logs/measurements") { request ->
            request.whenAuth {
                carbonLogRepository.getMeasurements().collectList()
                    .flatMap { ServerResponse.ok().body(BodyInserters.fromValue(it)) }
            }
        }

        POST("/carbon-logs/measurements") { request ->
            request.whenAuth {
                request.bodyToMono(CarbonMeasurement::class.java)
                    .flatMap { carbonLogRepository.insertMeasurement(it) }
                    .flatMap { ServerResponse.status(HttpStatus.CREATED).build() }
            }
        }
        POST("/carbon-logs/measurements/{co2Kg}") { request ->
            request.whenAuth {
                try {
                    request.pathVariable("co2Kg").toDouble().let {
                        carbonLogRepository.insertMeasurement(CarbonMeasurement(it, Instant.now()))
                            .flatMap { ServerResponse.status(HttpStatus.CREATED).build() }
                    }
                } catch (e: NumberFormatException) {
                    ServerResponse.badRequest().build()
                }
            }
        }
    }

    private fun ServerRequest.whenAuth(authedRequestHandler: (ServerRequest) -> Mono<ServerResponse>): Mono<ServerResponse> =
        when (isAuthed()) {
            true -> authedRequestHandler(this)
            else -> ServerResponse.status(HttpStatus.UNAUTHORIZED).build()
        }

    private fun ServerRequest.isAuthed(): Boolean = headers().header("Authorization")
        .find { it.startsWith("Basic") }
        ?.split("Basic ")
        ?.getOrNull(1)?.let { it == staticToken } ?: false
}
