package ch.burguiere.carbonlog.base

import ch.burguiere.carbonlog.converter.CarbonEquivalent
import java.time.Instant
import java.util.UUID

data class CarbonMeasurement(
    val id: String = UUID.randomUUID().toString(),
    val co2Kg: Double,
    val dt: Instant,
    val inputDescription: String? = null
) {
    companion object {
        fun of(carbonKg: Double): CarbonMeasurement = CarbonMeasurement(
            id = UUID.randomUUID().toString(),
            co2Kg = carbonKg,
            dt = Instant.now()
        )

        fun ofCarbonEquivalent(carbonEquivalent: CarbonEquivalent): CarbonMeasurement =
            CarbonMeasurement(
                id = UUID.randomUUID().toString(),
                co2Kg = carbonEquivalent.co2Kg,
                dt = Instant.now()
            )
    }

    fun asCarbonEquivalent(): CarbonEquivalent = CarbonEquivalent(co2Kg)
}
