package ch.burguiere.carbonlog.model

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CarbonMeasurementTest {
    @Test
    fun `should create measurement with iso dt field`() {
        val dt = LocalDateTime(2022, 1, 1, 13, 37, 42)
            .toInstant(TimeZone.UTC)
        val ms = CarbonMeasurementBuilder().co2Kg(42.0).dt(dt).build()

        assertEquals(ms.dtIso, "2022-01-01T13:37:42Z")
    }

}
