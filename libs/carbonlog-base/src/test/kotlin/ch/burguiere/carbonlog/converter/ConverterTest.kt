package ch.burguiere.carbonlog.converter

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test

class ConverterTest {

    @Test
    fun `should work`() {
        var ce = CarbonEquivalent(1.0)

        assertThat(ce.beefMeal()).isEqualTo(0.14)

        ce = CarbonEquivalent.ofBeefMeal(1.0)
        assertThat(ce.co2Kg).isCloseTo(7.14, Offset.offset(0.01))

        ce = CarbonEquivalent.ofCarKm(5.18)
        assertThat(ce.co2Kg).isCloseTo(1.0, Offset.offset(0.01))

        ce = CarbonEquivalent.ofChickenKg(0.1)
        assertThat(ce.co2Kg).isCloseTo(1.82, Offset.offset(0.01))

        ce = CarbonEquivalent.ofEggsKg(0.1)
        assertThat(ce.co2Kg).isCloseTo(0.53, Offset.offset(0.01))
    }
}
