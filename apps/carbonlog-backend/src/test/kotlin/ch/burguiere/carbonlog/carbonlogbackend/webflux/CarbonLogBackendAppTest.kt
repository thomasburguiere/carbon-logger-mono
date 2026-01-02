package ch.burguiere.carbonlog.carbonlogbackend.webflux

import ch.burguiere.carbonlog.base.CarbonMeasurement
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID

private val dummyToken = UUID.randomUUID().toString()

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class CarbonLogBackendAppTest {
    @Value(value = $$"${local.server.port}")
    private val port = 0

    @Autowired
    private lateinit var testClient: WebTestClient

    @Test
    fun `should run`() {
        val response = testClient
            .get().uri("http://localhost:$port")
            .exchange()

        response.expectStatus().is2xxSuccessful
        val responseBody = response.expectBody<String>().returnResult().responseBody
        assertThat(responseBody).isEqualTo("CarbonLogBackend is running")
    }

    @Test
    fun `should get 401 when not authenticated`() {
        val response = testClient
            .get().uri("http://localhost:$port/carbon-logs/measurements")
            .exchange()

        response.expectStatus().equals(401)
    }

    @Test
    fun `should CRUD measurement`() {
        val measurement = CarbonMeasurement(
            id = "testMeasurementId",
            co2Kg = 6.66,
            dt = ZonedDateTime.of(
                LocalDateTime.of(2022, 1, 1, 13, 37),
                ZoneId.of("UTC")
            ).toInstant(),
            inputDescription = "test measurement"
        )

        val postResponse = testClient
            .post()
            .uri("http://localhost:$port/carbon-logs/measurements")
            .bodyValue(measurement)
            .header("Authorization", "Basic $dummyToken")
            .exchange()

        postResponse.expectStatus().is2xxSuccessful

        val getResponse = testClient
            .get()
            .uri("http://localhost:$port/carbon-logs/measurements")
            .header("Authorization", "Basic $dummyToken")
            .exchange()

        getResponse.expectStatus().is2xxSuccessful
        val responseBody = getResponse.expectBody<List<CarbonMeasurement>>().returnResult().responseBody

        assertThat(responseBody).hasSize(1)
        assertThat(responseBody?.first()?.id).isEqualTo("testMeasurementId")
        assertThat(responseBody?.first()?.co2Kg).isEqualTo(6.66)
        assertThat(responseBody?.first()?.inputDescription).isEqualTo("test measurement")
        assertThat(responseBody?.first()?.dt.toString()).isEqualTo("2022-01-01T13:37:00Z")
    }

    companion object {
        @Container
        var mongoContainer: GenericContainer<*> = GenericContainer(DockerImageName.parse("mongo"))
            .withExposedPorts(27017)

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add(
                "mongourl.measurements.url",
                { "mongodb://${mongoContainer.host}:${mongoContainer.firstMappedPort}" }
            )
            registry.add("mongourl.measurements.db-name", { "dummyDB" })
            registry.add("static.auth.token", { dummyToken })
            registry.add("cors.allowed.origins", { "http://localhost:4200" })
        }
    }
}
