package ch.burguiere.carbonlog.base

import ch.burguiere.carbonlog.carbonlogconverter.CarbonEquivalent
import java.time.Instant

data class CarbonMeasurement(val co2Kg: Double, val dt: Instant) {
    companion object {
        fun of(carbonKg: Double): CarbonMeasurement {
            return CarbonMeasurement(carbonKg, Instant.now());
        }

        fun ofCarbonEquivalent(carbonEquivalent: CarbonEquivalent): CarbonMeasurement {
            return CarbonMeasurement(carbonEquivalent.co2Kg, Instant.now());
        }
    }
}
