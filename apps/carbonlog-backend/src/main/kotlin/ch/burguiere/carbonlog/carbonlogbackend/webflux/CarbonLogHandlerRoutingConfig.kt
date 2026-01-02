package ch.burguiere.carbonlog.carbonlogbackend.webflux

import ch.burguiere.carbonlog.base.CarbonMeasurement
import ch.burguiere.carbonlog.carbonlogbackend.repository.CarbonMeasurementsRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import java.time.Instant

@Configuration
open class CarbonLogHandlerRoutingConfig(
    private val carbonMeasurementsRepository: CarbonMeasurementsRepository,
    @param:Value("\${static.auth.token}") private val staticToken: String,
) {
    @Bean
    open fun configureRouting(): RouterFunction<ServerResponse> = router {

        GET("/") {
            ServerResponse.ok().bodyValue("CarbonLogBackend is running").toMono()
        }

        GET("/carbon-logs/measurements") { request ->
            request.whenAuth {
                carbonMeasurementsRepository.getMeasurements().collectList()
                    .then(ServerResponse.ok().body(BodyInserters.fromValue(it)))
            }
        }

        GET("/carbon-logs/measurements/{id}") { request ->
            request.whenAuth {
                val id = request.pathVariable("id")
                carbonMeasurementsRepository.getMeasurement(id)
                    .then(ServerResponse.ok().body(BodyInserters.fromValue(it)))
            }
        }

        DELETE("/carbon-logs/measurements/{id}") { request ->
            request.whenAuth {
                val id = request.pathVariable("id")
                carbonMeasurementsRepository.deleteMeasurement(id)
                    .then(ServerResponse.noContent().build())
            }
        }

        POST("/carbon-logs/measurements") { request ->
            request.whenAuth {
                request.bodyToMono<CarbonMeasurement>()
                    .flatMap { measurement ->
                        carbonMeasurementsRepository.insertMeasurement(measurement)
                            .then(
                                ServerResponse
                                    .status(CREATED)
                                    .header(
                                        "Location",
                                        "/carbon-logs/measurements/${measurement.id}"
                                    )
                                    .build()
                            )
                    }
            }
        }
        POST("/carbon-logs/measurements/{co2Kg}") { request ->
            request.whenAuth {
                try {
                    val co2Kg = request.pathVariable("co2Kg").toDouble()
                    val measurement = CarbonMeasurement(
                        co2Kg = co2Kg,
                        dt = Instant.now()
                    )
                    carbonMeasurementsRepository
                        .insertMeasurement(measurement)
                        .then(
                            ServerResponse
                                .status(CREATED)
                                .header(
                                    "Location",
                                    "/carbon-logs/measurements/${measurement.id}"
                                )
                                .build()
                        )
                } catch (_: NumberFormatException) {
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
