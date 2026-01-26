package ch.burguiere.carbonlog.model.jvm

import ch.burguiere.carbonlog.model.converter.CarbonEquivalent
import ch.burguiere.carbonlog.model.converter.asCarbonEquivalent
import ch.burguiere.carbonlog.model.jvm.CarbonMeasurement.Companion.ofCarbonEquivalent
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.offset
import kotlin.test.Test
import kotlin.time.ExperimentalTime

class CarbonMeasurementJvmTest {
    @Test
    fun `should work`() {
        var ms = CarbonMeasurement.of(0.0)

        assertThat(ms.co2Kg).isEqualTo(0.0)

        ms = ofCarbonEquivalent(CarbonEquivalent.ofPlaneKm(2000.0))
        assertThat(ms.co2Kg).isCloseTo(371.75, offset(0.01))

        assertThat(ms.asCarbonEquivalent().planeKm()).isCloseTo(2000.0, offset(0.01))
    }

    @OptIn(ExperimentalTime::class)
    @Test
    fun `should convert a common measurement to a jvm one`() {
        val dt = kotlinx.datetime.LocalDateTime(2022, 1, 1, 13, 37, 42)
            .toInstant(TimeZone.UTC)


        val commonMs = ch.burguiere.carbonlog.model.CarbonMeasurement.of(co2Kg = 42.0, dt = dt)

        val ms: CarbonMeasurement = CarbonMeasurement.ofCommonCarbonMeasurement(commonMs)

        assertThat(ms.dt.toString()).isEqualTo("2022-01-01T13:37:42Z")
        assertThat(ms.co2Kg).isEqualTo(42.0)
    }
}
