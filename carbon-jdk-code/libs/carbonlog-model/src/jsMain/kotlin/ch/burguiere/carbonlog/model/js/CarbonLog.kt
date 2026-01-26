@file:OptIn(ExperimentalTime::class, ExperimentalJsExport::class)

package ch.burguiere.carbonlog.model.js

import ch.burguiere.carbonlog.model.CarbonMeasurement
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@JsExport
data class CarbonLog(val carbonMeasurements: Array<CarbonMeasurement>) {
    fun copyAdding(measurement: CarbonMeasurement) = copyAddingMultiple(measurements = arrayOf(measurement))

    fun copyAddingMultiple(measurements: Array<CarbonMeasurement>): CarbonLog =
        CarbonLog(carbonMeasurements = this.carbonMeasurements + measurements)

    fun getRangeCarbonKgs(
        fromIso: String,
        toIso: String = Clock.System.now().toString(),
        inclusive: Boolean = false
    ): Double {
        val from = Instant.parse(fromIso)
        val to = Instant.parse(toIso)


        val filter: (cm: CarbonMeasurement) -> Boolean = if (!inclusive) {
            { cm: CarbonMeasurement -> cm.dt > from && cm.dt < to }
        } else {
            { cm: CarbonMeasurement -> cm.dt in from..to }
        }

        val filtered = this.carbonMeasurements.filter(filter)
        return filtered.toTypedArray().sumCO2Kgs()
    }

    fun getTotalCarbonKgs(): Double = carbonMeasurements.sumCO2Kgs()

    fun getCurrentYearCarbonKgs(): Double = carbonMeasurements
        .filter { cm: CarbonMeasurement -> cm.getYear() == Clock.System.now().toLocalDateTime(TimeZone.UTC).year }
        .toTypedArray()
        .sumCO2Kgs()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CarbonLog

        if (!carbonMeasurements.contentEquals(other.carbonMeasurements)) return false

        return true
    }

    override fun hashCode(): Int {
        return carbonMeasurements.contentHashCode()
    }
}

private val CarbonMeasurement.dt: Instant get() = Instant.parse(this.dtIso)

private fun CarbonMeasurement.getYear(timeZone: TimeZone = TimeZone.UTC): Int = dt.toLocalDateTime(timeZone).year

private fun Array<CarbonMeasurement>.sumCO2Kgs(): Double =
    when {
        this.isEmpty() -> 0.0
        else -> this.map { cm -> cm.co2Kg }
            .reduce { acc, next -> acc + next }
    }
