package ch.burguiere.carbonlog.base

import ch.burguiere.carbonlog.carbonlogconverter.CarbonEquivalent
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class CarbonMeasurementTest {
    @Test
    fun `should work`() {

        var ms = CarbonMeasurement.of(0.0)

        Assertions.assertThat(ms.co2Kg).isEqualTo(0.0)

        ms = CarbonMeasurement.ofCarbonEquivalent(CarbonEquivalent.ofPlaneKm(2000.0))
        Assertions.assertThat(ms.co2Kg).isCloseTo(371.75, Offset.offset(0.01))
    }
}
