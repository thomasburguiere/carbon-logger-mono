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
    fun `should CRUD measurement`(){
        val postResponse = testClient
            .post()
            .uri("http://localhost:$port/carbon-logs/measurements/3.6")
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
        assertThat(responseBody?.first()?.co2Kg).isEqualTo(3.6)
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
