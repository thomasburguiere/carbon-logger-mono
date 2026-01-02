package ch.burguiere.carbonlog.carbonlogbackend.repository

import ch.burguiere.carbonlog.base.CarbonMeasurement
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


interface CarbonMeasurementsRepository {
    enum class Fields(val title: String) {
        ID("id"),
        CO2KG("co2Kg"),
        TIMESTAMP("timestamp"),
        INPUT_DESCRIPTION("inputDescription"),
    }

    fun getMeasurements(): Flux<CarbonMeasurement>
    fun getMeasurement(id: String): Mono<CarbonMeasurement>
    fun insertMeasurement(measurement: CarbonMeasurement): Mono<Void>
    fun deleteMeasurement(id: String): Mono<Void>
}
