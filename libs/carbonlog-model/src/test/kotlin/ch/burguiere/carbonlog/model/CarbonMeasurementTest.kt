package ch.burguiere.carbonlog.model

import ch.burguiere.carbonlog.model.CarbonMeasurement.Companion.ofCarbonEquivalent
import ch.burguiere.carbonlog.model.converter.CarbonEquivalent
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.offset
import org.junit.jupiter.api.Test

class CarbonMeasurementTest {
    @Test
    fun `should work`() {

        var ms = CarbonMeasurement.of(0.0)

        assertThat(ms.co2Kg).isEqualTo(0.0)

        ms = ofCarbonEquivalent(CarbonEquivalent.ofPlaneKm(2000.0))
        assertThat(ms.co2Kg).isCloseTo(371.75, offset(0.01))

        assertThat(ms.asCarbonEquivalent().planeKm()).isCloseTo(2000.0, offset(0.01))
    }
}
