package ch.burguiere.carbonlog.base

import ch.burguiere.carbonlog.carbonlogconverter.CarbonEquivalent
import java.time.Instant

data class CarbonMeasurement(val co2Kg: Double, val dt: Instant, val inputDescription: String? = null) {
    companion object {
        fun of(carbonKg: Double): CarbonMeasurement = CarbonMeasurement(carbonKg, Instant.now())

        fun ofCarbonEquivalent(carbonEquivalent: CarbonEquivalent): CarbonMeasurement =
            CarbonMeasurement(carbonEquivalent.co2Kg, Instant.now())
    }

    fun asCarbonEquivalent(): CarbonEquivalent = CarbonEquivalent(co2Kg)
}
