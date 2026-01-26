package ch.burguiere.carbonlog.model.jvm

import ch.burguiere.carbonlog.model.CoreCarbonMeasurement
import ch.burguiere.carbonlog.model.converter.CarbonEquivalent
import java.time.Instant
import java.util.UUID

data class CarbonMeasurement(
    override val id: String = UUID.randomUUID().toString(),
    override val co2Kg: Double,
    val dt: Instant,
    override val inputDescription: String? = null
) : CoreCarbonMeasurement {
    companion object {
        fun ofCommonCarbonMeasurement(m: ch.burguiere.carbonlog.model.CarbonMeasurement): CarbonMeasurement =
            CarbonMeasurement(
                id = m.id,
                co2Kg = m.co2Kg,
                dt = Instant.parse(m.dtIso),
                inputDescription = m.inputDescription
            )

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
}

