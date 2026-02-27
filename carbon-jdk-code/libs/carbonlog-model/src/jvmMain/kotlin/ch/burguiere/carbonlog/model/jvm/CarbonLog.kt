package ch.burguiere.carbonlog.model.jvm

import ch.burguiere.carbonlog.model.CoreCarbonLog
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class CarbonLog(val carbonMeasurements: List<CarbonMeasurement>): CoreCarbonLog<Instant> {
    fun copyAdding(measurement: CarbonMeasurement) = copyAddingMultiple(measurements = listOf(measurement))

    fun copyAddingMultiple(measurements: List<CarbonMeasurement>): CarbonLog =
        CarbonLog(carbonMeasurements = this.carbonMeasurements + measurements)

    override fun getRangeCarbonKgs(from: Instant, to: Instant, inclusive: Boolean): Double {
        val filter: (cm: CarbonMeasurement) -> Boolean = if (!inclusive) {
            { cm: CarbonMeasurement -> cm.dt > from && cm.dt < to }
        } else {
            { cm: CarbonMeasurement -> cm.dt in from..to }
        }

        val filtered = this.carbonMeasurements.filter(filter)
        return filtered.sumCO2Kgs()
    }

    override fun getTotalCarbonKgs(): Double = carbonMeasurements.sumCO2Kgs()

    override fun getCurrentYearCarbonKgs(): Double = carbonMeasurements
        .filter { cm: CarbonMeasurement -> cm.getYear() == LocalDate.now().year }
        .sumCO2Kgs()
}

private fun CarbonMeasurement.getYear(timezoneId: ZoneId = ZoneId.of("UTC")): Int =
    LocalDate.ofInstant(this.dt, timezoneId).year

private fun List<CarbonMeasurement>.sumCO2Kgs(): Double =
    when {
        this.isEmpty() -> 0.0
        else -> this.map { cm -> cm.co2Kg }
            .reduce { acc, next -> acc + next }
    }
