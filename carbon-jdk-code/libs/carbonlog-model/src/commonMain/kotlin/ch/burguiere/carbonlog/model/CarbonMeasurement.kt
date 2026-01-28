@file:OptIn(ExperimentalJsExport::class, ExperimentalUuidApi::class, ExperimentalTime::class)

package ch.burguiere.carbonlog.model

import ch.burguiere.carbonlog.model.converter.CarbonEquivalent
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@JsExport
interface CoreCarbonMeasurement {
    val id: String
    val co2Kg: Double
    val inputDescription: String?
}

@JsExport
data class CarbonMeasurement(
    override val id: String = Uuid.random().toString(),
    override val co2Kg: Double,
    val dtIso: String = Clock.System.now().toString(),
    override val inputDescription: String? = null
) : CoreCarbonMeasurement {
    companion object {
        fun ofCarbonEquivalent(carbonEquivalent: CarbonEquivalent): CarbonMeasurement =
            CarbonMeasurement(
                id = Uuid.random().toString(),
                co2Kg = carbonEquivalent.co2Kg,
                dtIso = Clock.System.now().toString()
            )
    }
}

@JsExport
data class CarbonMeasurementBuilder(
    var internalId: String? = null,
    var internalCo2Kg: Double? = null,
    var internalDtIso: String? = null,
    var internalInputDescription: String? = null
) {
    fun id(id: String) = apply { this.internalId = id }
    fun co2Kg(co2Kg: Double) = apply { this.internalCo2Kg = co2Kg }
    fun dtIso(dtIso: String) = apply { this.internalDtIso = dtIso }
    fun inputDescription(inputDescription: String) = apply { this.internalInputDescription = inputDescription }


    fun build(): CarbonMeasurement {
        if(internalCo2Kg == null) {
            error("builder.co2Kg cannot be null")
        }
        return CarbonMeasurement(
            id = internalId ?: Uuid.random().toString(),
            co2Kg = internalCo2Kg!!,
            dtIso = internalDtIso ?: Clock.System.now().toString(),
            inputDescription = internalInputDescription
        )
    }

}

fun CarbonMeasurementBuilder.dt(dt: Instant) = apply { this.internalDtIso = dt.toString() }

fun CarbonMeasurement.Companion.of(
    id: String = Uuid.random().toString(),
    co2Kg: Double,
    dt: Instant = Clock.System.now(),
    inputDescription: String? = null
): CarbonMeasurement = CarbonMeasurement(
    id = id,
    co2Kg = co2Kg,
    dtIso = dt.toString(),
    inputDescription = inputDescription
)
