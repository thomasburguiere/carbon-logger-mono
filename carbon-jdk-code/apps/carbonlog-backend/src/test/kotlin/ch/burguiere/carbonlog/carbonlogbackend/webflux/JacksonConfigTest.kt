package ch.burguiere.carbonlog.carbonlogbackend.webflux

import ch.burguiere.carbonlog.model.CarbonMeasurement
import ch.burguiere.carbonlog.carbonlogbackend.json.JacksonConfig
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
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
        assertThat(parsed.inputDescription.get()).isEqualTo("kurwa")
    }

    @Test
    fun `should serialize`() {
        val measurement = CarbonMeasurement.Builder()
            .id("test")
            .co2Kg(6.66)
            .dt(Instant.parse("2022-11-27T11:00:01.759Z"))
            .inputDescription("comment")
            .build()

        val json = jsonMapper.writeValueAsString(measurement)
        assertThatJson(json).isEqualTo(
            """{
                    "id": "test",
                    "co2Kg": 6.66,
                    "dt": "2022-11-27T11:00:01.759Z",
                    "inputDescription": "comment"
                }""".trimIndent()
        )
    }

    @Test
    fun `should serialize with empty description`() {
        val measurement = CarbonMeasurement.Builder()
            .id("test")
            .co2Kg(6.66)
            .dt(Instant.parse("2022-11-27T11:00:01.759Z"))
            .build()

        val json = jsonMapper.writeValueAsString(measurement)
        assertThatJson(json).isEqualTo(
            """{
                    "id": "test",
                    "co2Kg": 6.66,
                    "dt": "2022-11-27T11:00:01.759Z",
                    "inputDescription": null
                }""".trimIndent()
        )
    }



    @Test
    fun `should deserialize with no description`() {
        val json = """
            {
              "co2Kg": 6.42,
              "dt": "2022-11-27T11:00:01.759Z"
            }
        """.trimIndent()

        val parsed: CarbonMeasurement = jsonMapper.readValue(json, CarbonMeasurement::class.java)

        assertThat(parsed.co2Kg).isEqualTo(6.42)
        assertThat(parsed.dt.toString()).isEqualTo("2022-11-27T11:00:01.759Z")
        assertThat(parsed.inputDescription).isEmpty()
    }

    @Test
    fun `should deserialize with null description`() {
        val json = """
            {
              "co2Kg": 6.42,
              "dt": "2022-11-27T11:00:01.759Z",
              "inputDescription": null
            }
        """.trimIndent()

        val parsed: CarbonMeasurement = jsonMapper.readValue(json, CarbonMeasurement::class.java)

        assertThat(parsed.co2Kg).isEqualTo(6.42)
        assertThat(parsed.dt.toString()).isEqualTo("2022-11-27T11:00:01.759Z")
        assertThat(parsed.inputDescription).isEmpty()
    }

}
