package ch.burguiere.carbonlog.carbonlogbackend.webflux

import ch.burguiere.carbonlog.base.CarbonMeasurement
import ch.burguiere.carbonlog.carbonlogbackend.repository.CarbonMeasurementsRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.UNAUTHORIZED
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

    val log: Logger = LoggerFactory.getLogger(CarbonLogHandlerRoutingConfig::class.java)

    @Bean
    open fun configureRouting(): RouterFunction<ServerResponse> = router {

        GET("/") {
            ServerResponse.ok().bodyValue("CarbonLogBackend is running").toMono()
        }

        GET("/carbon-logs/measurements") { request ->
            request.whenAuth {
                carbonMeasurementsRepository.getMeasurements().collectList()
                    .flatMap { measurements ->
                        ServerResponse
                            .ok()
                            .body(BodyInserters.fromValue(measurements))
                    }
            }
        }

        GET("/carbon-logs/measurements/{id}") { request ->
            request.whenAuth {
                val id = request.pathVariable("id")
                carbonMeasurementsRepository.getMeasurement(id)
                    .flatMap { measurement ->
                        ServerResponse
                            .ok()
                            .body(BodyInserters.fromValue(measurement))
                    }
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
                    .flatMap { measurement -> insertMeasurementAndReturn201(measurement) }
            }
        }
        POST("/carbon-logs/measurements/{co2Kg}") { request ->
            request.whenAuth {
                val co2KgPathVar: String = request.pathVariable("co2Kg")

                val co2Kg: Double = try {
                    co2KgPathVar.toDouble()
                } catch (e: NumberFormatException) {
                    val msg = "Tried to create measurement with invalid co2Kg number: $co2KgPathVar"
                    log.warn(msg, e)
                    return@whenAuth ServerResponse
                        .badRequest()
                        .bodyValue(msg)
                }
                val measurement = CarbonMeasurement(
                    co2Kg = co2Kg,
                    dt = Instant.now()
                )
                insertMeasurementAndReturn201(measurement)
            }
        }
    }

    private fun insertMeasurementAndReturn201(measurement: CarbonMeasurement): Mono<ServerResponse> =
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

    private fun ServerRequest.whenAuth(authedRequestHandler: () -> Mono<ServerResponse>): Mono<ServerResponse> =
        when (isAuthed()) {
            true -> authedRequestHandler()
            else -> {
                log.warn("Unauthorized access for ${this.method()} ${this.path()}")
                ServerResponse.status(UNAUTHORIZED).build()
            }
        }

    private fun ServerRequest.isAuthed(): Boolean = headers().header("Authorization")
        .find { it.startsWith("Basic") }
        ?.split("Basic ")
        ?.getOrNull(1)?.let { it == staticToken } ?: false
}
