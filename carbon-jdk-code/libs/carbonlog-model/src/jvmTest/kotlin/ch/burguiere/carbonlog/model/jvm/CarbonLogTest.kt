package ch.burguiere.carbonlog.model.jvm

import org.assertj.core.api.Assertions.assertThat
import java.time.Instant
import java.time.ZoneOffset
import kotlin.test.Test

class CarbonLogTest {
    private val date1: java.time.LocalDate = java.time.LocalDate.parse("2022-01-01")
    private val date2: java.time.LocalDate = java.time.LocalDate.parse("2022-01-02")
    private val date3: java.time.LocalDate = java.time.LocalDate.parse("2022-01-03")
    private val date4: java.time.LocalDate = java.time.LocalDate.parse("2022-01-04")
    private val date_2021: java.time.LocalDate = java.time.LocalDate.parse("2021-01-04")


    @Test
    fun `should get results when in range`() {
        // given
        val cm2 = CarbonMeasurement(co2Kg = 2.0, dt = date2.atStartOfDay().toInstant(ZoneOffset.UTC))
        val cm3 = CarbonMeasurement(co2Kg = 3.0, dt = date3.atStartOfDay().toInstant(ZoneOffset.UTC))
        val log = CarbonLog(listOf(cm2, cm3))

        // when
        val result = log.getRangeCarbonKgs(
            date1.atStartOfDay().toInstant(ZoneOffset.UTC),
            date4.atStartOfDay().toInstant(ZoneOffset.UTC)
        )

        // then
        assertThat(result).isEqualTo(5.0)
    }

    @Test
    fun `should get results when in range with added data`() {
        // given
        val cm2 = CarbonMeasurement(co2Kg = 2.0, dt = date2.atStartOfDay().toInstant(ZoneOffset.UTC))
        val cm3 = CarbonMeasurement(co2Kg = 3.0, dt = date3.atStartOfDay().toInstant(ZoneOffset.UTC))
        var log = CarbonLog(listOf(cm2, cm3))

        val addedCm = CarbonMeasurement(co2Kg = 11.0, dt = date2.atStartOfDay().toInstant(ZoneOffset.UTC))

        log = log.copyAdding(addedCm)

        // when
        val result = log.getRangeCarbonKgs(
            date1.atStartOfDay().toInstant(ZoneOffset.UTC),
            date4.atStartOfDay().toInstant(ZoneOffset.UTC)
        )

        // then
        assertThat(result).isEqualTo(16.0)
    }

    @Test
    fun `should get nothing when outside range`() {
        // given
        val cm2 = CarbonMeasurement(co2Kg = 2.0, dt = date2.atStartOfDay().toInstant(ZoneOffset.UTC))
        val cm3 = CarbonMeasurement(co2Kg = 3.0, dt = date3.atStartOfDay().toInstant(ZoneOffset.UTC))
        val log = CarbonLog(listOf(cm2, cm3))

        // when
        val result = log.getRangeCarbonKgs(
            date4.atStartOfDay().toInstant(ZoneOffset.UTC),
            date4.atStartOfDay().toInstant(ZoneOffset.UTC)
        )

        // then
        assertThat(result).isEqualTo(0.0)
    }

    @Test
    fun `should get nothing when search range exclusive`() {
        // given
        val cm2 = CarbonMeasurement(co2Kg = 2.0, dt = date2.atStartOfDay().toInstant(ZoneOffset.UTC))
        val cm3 = CarbonMeasurement(co2Kg = 3.0, dt = date3.atStartOfDay().toInstant(ZoneOffset.UTC))
        val log = CarbonLog(listOf(cm2, cm3))

        // when
        val result = log.getRangeCarbonKgs(
            date3.atStartOfDay().toInstant(ZoneOffset.UTC),
            date4.atStartOfDay().toInstant(ZoneOffset.UTC)
        )

        // then
        assertThat(result).isEqualTo(0.0)
    }

    @Test
    fun `should get result when search range inclusive`() {
        // given
        val cm2 = CarbonMeasurement(co2Kg = 2.0, dt = date2.atStartOfDay().toInstant(ZoneOffset.UTC))
        val cm3 = CarbonMeasurement(co2Kg = 3.0, dt = date3.atStartOfDay().toInstant(ZoneOffset.UTC))
        val log = CarbonLog(listOf(cm2, cm3))

        // when
        val result = log.getRangeCarbonKgs(
            date3.atStartOfDay().toInstant(ZoneOffset.UTC),
            date4.atStartOfDay().toInstant(ZoneOffset.UTC),
            true
        )

        // then
        assertThat(result).isEqualTo(3.0)
    }

    @Test
    fun `should get result for current year`() {
        // given
        val todayDate = Instant.now()
        val cm2 = CarbonMeasurement(co2Kg = 2.0, dt = todayDate)
        val cm3 = CarbonMeasurement(co2Kg = 3.0, dt = date_2021.atStartOfDay().toInstant(ZoneOffset.UTC))
        val log = CarbonLog(listOf(cm2, cm3))

        // when
        val result = log.getCurrentYearCarbonKgs()

        // then
        assertThat(result).isEqualTo(cm2.co2Kg)
    }

    @Test
    fun `should get total CO2 Kg`() {
        val cm2 = CarbonMeasurement(co2Kg = 2.0, dt = Instant.now())
        val cm3 = CarbonMeasurement(co2Kg = 3.0, dt = Instant.now())
        val log = CarbonLog(listOf(cm2, cm3))

        assertThat(log.getTotalCarbonKgs()).isEqualTo(5.0)

    }
}
