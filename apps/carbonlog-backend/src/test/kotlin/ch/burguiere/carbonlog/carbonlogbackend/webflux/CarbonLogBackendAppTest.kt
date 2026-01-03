package ch.burguiere.carbonlog.carbonlogbackend.webflux

import ch.burguiere.carbonlog.model.CarbonMeasurement
import ch.burguiere.carbonlog.carbonlogbackend.repository.CarbonMeasurementsRepository
import ch.burguiere.carbonlog.carbonlogbackend.repository.MongoCarbonMeasurementsRepository
import com.mongodb.reactivestreams.client.MongoDatabase
import org.assertj.core.api.Assertions.assertThat
import org.bson.BsonDocument
import org.junit.jupiter.api.AfterEach
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
import reactor.kotlin.core.publisher.toMono
import reactor.test.StepVerifier
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

    @Autowired
    private lateinit var database: MongoDatabase

    @Autowired
    private lateinit var measurementsRepository: CarbonMeasurementsRepository

    @AfterEach
    fun afterEach() {
        val clearMono = database
            .getCollection(MongoCarbonMeasurementsRepository.collectionName)
            .deleteMany(BsonDocument())
            .toMono()
            .then()
        StepVerifier.create(clearMono).verifyComplete()
    }

    @Test
    fun `should run`() {
        val response = testClient
            .get().uri("http://localhost:$port/healthz")
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
    fun `should create measurement without req body`() {
        val postResponse = testClient
            .post()
            .uri("http://localhost:$port/carbon-logs/measurements/42.0")
            .header("Authorization", "Bearer $dummyToken")
            .exchange()

        postResponse.expectStatus().isEqualTo(201)
        val id = postResponse.returnResult()
            .responseHeaders["Location"]?.first()
            ?.split("/")?.last()

        assertThat(id).isNotNull

        StepVerifier.create(measurementsRepository.getMeasurement(id!!))
            .assertNext { measurement ->
                assertThat(measurement.co2Kg).isEqualTo(42.0)
            }
            .verifyComplete()
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
            .header("Authorization", "Bearer $dummyToken")
            .exchange()

        postResponse.expectStatus().isEqualTo(201)
        postResponse.expectHeader().location("/carbon-logs/measurements/testMeasurementId")

        val getResponse = testClient
            .get()
            .uri("http://localhost:$port/carbon-logs/measurements/${measurement.id}")
            .header("Authorization", "Bearer $dummyToken")
            .exchange()

        getResponse.expectStatus().is2xxSuccessful
        val getMeasurementBody = getResponse.expectBody<CarbonMeasurement>().returnResult().responseBody

        assertThat(getMeasurementBody?.id).isEqualTo(measurement.id)
        assertThat(getMeasurementBody?.co2Kg).isEqualTo(6.66)
        assertThat(getMeasurementBody?.inputDescription).isEqualTo("test measurement")
        assertThat(getMeasurementBody?.dt.toString()).isEqualTo("2022-01-01T13:37:00Z")

        val deleteResponse = testClient
            .delete()
            .uri("http://localhost:$port/carbon-logs/measurements/${measurement.id}")
            .header("Authorization", "Bearer $dummyToken")
            .exchange()

        deleteResponse.expectStatus().isEqualTo(204)

        val getAllResponse = testClient
            .get()
            .uri("http://localhost:$port/carbon-logs/measurements")
            .header("Authorization", "Bearer $dummyToken")
            .exchange()

        val getAllBody = getAllResponse.expectBody<List<CarbonMeasurement>>().returnResult().responseBody
        assertThat(getAllBody).isEmpty()
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
