package ch.burguiere.carbonlog.carbonlogbackend.repository

import ch.burguiere.carbonlog.base.CarbonMeasurement
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CarbonLogRepository {
    fun getMeasurements(): Flux<CarbonMeasurement>
    fun insertMeasurement(measurement: CarbonMeasurement): Mono<Void>
}
