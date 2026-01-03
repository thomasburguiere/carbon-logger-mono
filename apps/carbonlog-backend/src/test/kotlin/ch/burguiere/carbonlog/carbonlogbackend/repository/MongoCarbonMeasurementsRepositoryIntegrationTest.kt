package ch.burguiere.carbonlog.carbonlogbackend.repository

import ch.burguiere.carbonlog.model.CarbonMeasurement
import com.mongodb.reactivestreams.client.MongoDatabase
import org.assertj.core.api.Assertions.assertThat
import org.bson.BsonDocument
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import reactor.kotlin.core.publisher.toMono
import reactor.test.StepVerifier
import java.time.Instant

@Testcontainers
@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [RepoConfig::class])
class MongoCarbonMeasurementsRepositoryIntegrationTest {


    @Autowired
    private lateinit var repo: CarbonMeasurementsRepository

    @Autowired
    private lateinit var db: MongoDatabase

    companion object {
        @Container
        var mongoContainer: GenericContainer<*> = GenericContainer(DockerImageName.parse("mongo"))
            .withExposedPorts(27017)

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("mongourl.measurements.url") { "mongodb://${mongoContainer.host}:${mongoContainer.firstMappedPort}" }
            registry.add("mongourl.measurements.db-name") { "dummyDBName" }
        }
    }

    @AfterEach
    fun afterEach() {
        StepVerifier.create(
            db.getCollection(MongoCarbonMeasurementsRepository.collectionName)
                .deleteMany(BsonDocument())
                .toMono()
                .then()
        ).verifyComplete()
    }

    @Test
    fun `should get measurements`() {
        val result = repo.getMeasurements()

        StepVerifier.create(result.collectList())
            .assertNext { list -> assertThat(list).hasSize(0) }
            .verifyComplete()
    }

    @Test
    fun `should prevent creation of measurements with same id`() {
        val measurement1 = CarbonMeasurement(
            id = "duplicateId",
            co2Kg = 3.33,
            dt = Instant.now()
        )
        val measurement2 = CarbonMeasurement(
            id = "duplicateId",
            co2Kg = 6.66,
            dt = Instant.now()
        )

        StepVerifier.create(repo.insertMeasurement(measurement1))
            .verifyComplete()
        StepVerifier.create(repo.insertMeasurement(measurement2))
            .verifyError()

        StepVerifier.create(repo.getMeasurements().collectList())
            .assertNext { list -> assertThat(list).hasSize(1) }
            .verifyComplete()
    }

    @Test
    fun `should delete`() {
        val measurement1 = CarbonMeasurement(
            id = "duplicateId",
            co2Kg = 3.33,
            dt = Instant.now()
        )

        StepVerifier.create(repo.insertMeasurement(measurement1))
            .verifyComplete()

        StepVerifier.create(repo.getMeasurements().collectList())
            .assertNext { list -> assertThat(list).hasSize(1) }
            .verifyComplete()

        StepVerifier.create(repo.deleteMeasurement(measurement1.id))
            .verifyComplete()

        StepVerifier.create(repo.getMeasurements().collectList())
            .assertNext { list -> assertThat(list).isEmpty() }
            .verifyComplete()
    }
}
