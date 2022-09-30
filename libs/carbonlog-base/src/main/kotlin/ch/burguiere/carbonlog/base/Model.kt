package ch.burguiere.carbonlog.base

import ch.burguiere.carbonlog.carbonlogconverter.CarbonEquivalent
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class CarbonMeasurement(val co2Kg: Double, val dt: Instant) {
    companion object {
        fun of(carbonKg: Double): CarbonMeasurement {
            return CarbonMeasurement(carbonKg, Instant.now());
        }

        fun ofCarbonEquivalent(carbonEquivalent: CarbonEquivalent): CarbonMeasurement {
            return CarbonMeasurement(carbonEquivalent.co2Kg, Instant.now());
        }
    }

    fun asCarbonEquivalent(): CarbonEquivalent {
        return CarbonEquivalent(co2Kg);
    }
}


data class CarbonLog(val carbonMeasurements: MutableList<CarbonMeasurement>) {
    fun add(measurement: CarbonMeasurement) {
        this.carbonMeasurements.add(measurement);
    }

    fun addMultiple(measurements: List<CarbonMeasurement>) {
        this.carbonMeasurements.addAll(measurements);
    }


    fun getRangeCarbonKgs(from: Instant, to: Instant = Instant.now(), inclusive: Boolean = false): Double {
        val filter: (cm: CarbonMeasurement) -> Boolean = if (!inclusive) {
            { cm: CarbonMeasurement -> cm.dt > from && cm.dt < to };
        } else {
            { cm: CarbonMeasurement -> cm.dt in from..to };
        }

        val filtered = this.carbonMeasurements.filter(filter);
        return filtered.sumCO2Kgs();
    }

    fun getTotalCarbonKgs(): Double = carbonMeasurements.sumCO2Kgs()

    //
    fun getCurrentYearCarbonKgs(): Double = carbonMeasurements
        .filter { cm: CarbonMeasurement -> cm.getYear() == LocalDate.now().year }
        .sumCO2Kgs()
}

private fun CarbonMeasurement.getYear(): Int = LocalDate.ofInstant(this.dt, ZoneId.of("UTC")).year

private fun List<CarbonMeasurement>.sumCO2Kgs(): Double =
    when (this.isEmpty()) {
        true -> 0.0
        else -> this.map { cm -> cm.co2Kg }
            .reduce { acc, next -> acc + next }
    }
