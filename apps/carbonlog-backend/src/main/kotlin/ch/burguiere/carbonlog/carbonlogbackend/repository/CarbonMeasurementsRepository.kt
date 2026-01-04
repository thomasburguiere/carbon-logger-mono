package ch.burguiere.carbonlog.carbonlogbackend.repository

import ch.burguiere.carbonlog.model.CarbonMeasurement
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


interface CarbonMeasurementsRepository {
    fun getMeasurements(): Flux<CarbonMeasurement>
    fun getMeasurement(id: String): Mono<CarbonMeasurement>
    fun insertMeasurement(measurement: CarbonMeasurement): Mono<Void>
    fun deleteMeasurement(id: String): Mono<Void>
    fun updateMeasurement(id: String, measurement: CarbonMeasurement): Mono<Void>
}
