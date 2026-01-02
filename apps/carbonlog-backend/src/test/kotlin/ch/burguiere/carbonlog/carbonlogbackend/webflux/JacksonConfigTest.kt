package ch.burguiere.carbonlog.carbonlogbackend.webflux

import ch.burguiere.carbonlog.base.CarbonMeasurement
import ch.burguiere.carbonlog.carbonlogbackend.json.JacksonConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant

class JacksonConfigTest {

    val jsonMapper = JacksonConfig().customJsonMapper()

    @Test
    fun `should deserialize`() {
        val json = """
            {
              "co2Kg": 6.42,
              "dt": "2022-11-27T11:00:01.759Z",
              "inputDescription": "kurwa"
            }
        """.trimIndent()

        val parsed: CarbonMeasurement = jsonMapper.readValue(json, CarbonMeasurement::class.java)

        assertThat(parsed.co2Kg).isEqualTo(6.42)
        assertThat(parsed.dt.toString()).isEqualTo("2022-11-27T11:00:01.759Z")
        assertThat(parsed.inputDescription).isEqualTo("kurwa")
    }

    @Test
    fun `should serialize`() {
        val measurement = CarbonMeasurement(
            id = "test",
            co2Kg = 6.66,
            dt = Instant.parse("2022-11-27T11:00:01.759Z"),
            inputDescription = "comment"
        )

        val json = jsonMapper.writeValueAsString(measurement)
        assertThat(json).isEqualTo(
            """{"id":"test","co2Kg":6.66,"dt":"2022-11-27T11:00:01.759Z","inputDescription":"comment"}"""
        )
    }
}
