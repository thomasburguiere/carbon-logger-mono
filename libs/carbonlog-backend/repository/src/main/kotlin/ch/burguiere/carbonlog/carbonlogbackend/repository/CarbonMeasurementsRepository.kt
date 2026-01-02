package ch.burguiere.carbonlog.carbonlogbackend.repository

import ch.burguiere.carbonlog.base.CarbonMeasurement
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

enum class Fields(name: String) {
    ID("id"),
    CO2KG("co2Kg"),
}

interface CarbonMeasurementsRepository {
    fun getMeasurements(): Flux<CarbonMeasurement>
    fun getMeasurement(id: String): Mono<CarbonMeasurement>
    fun insertMeasurement(measurement: CarbonMeasurement): Mono<Void>
}
