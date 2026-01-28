package ch.burguiere.carbonlog.model.jvm.converter

import ch.burguiere.carbonlog.model.converter.CarbonEquivalent
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.offset
import kotlin.test.Test

class ConverterTest {

    @Test
    fun `should work`() {
        var ce = CarbonEquivalent(1.0)

        assertThat(ce.beefMeal()).isEqualTo(0.14)

        ce = CarbonEquivalent.ofBeefMeal(1.0)
        assertThat(ce.co2Kg).isCloseTo(7.14, offset(0.01))

        ce = CarbonEquivalent.ofCarKm(5.18)
        assertThat(ce.co2Kg).isCloseTo(1.0, offset(0.01))

        ce = CarbonEquivalent.ofChickenKg(0.1)
        assertThat(ce.co2Kg).isCloseTo(1.82, offset(0.01))

        ce = CarbonEquivalent.ofEggsKg(0.1)
        assertThat(ce.co2Kg).isCloseTo(0.53, offset(0.01))
    }
}
