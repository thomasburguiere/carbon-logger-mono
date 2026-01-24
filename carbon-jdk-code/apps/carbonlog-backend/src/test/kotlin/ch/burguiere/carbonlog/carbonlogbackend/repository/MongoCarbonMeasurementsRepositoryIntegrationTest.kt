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
import java.time.temporal.ChronoUnit
import java.util.Optional

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
        val measurement1 = CarbonMeasurement.Builder()
            .id("duplicateId")
            .co2Kg(3.33)
            .dt(Instant.now())
            .build()
        val measurement2 = CarbonMeasurement.Builder()
            .id("duplicateId")
            .co2Kg(6.66)
            .dt(Instant.now())
            .build()

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
        val measurement1 = CarbonMeasurement.Builder()
            .id("duplicateId")
            .co2Kg(3.33)
            .dt(Instant.now())
            .build()

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

    @Test
    fun `should update, allowing passing of null description`() {
        val measurement = CarbonMeasurement.Builder()
            .id("updated-measurement-id")
            .co2Kg(3.33)
            .dt(Instant.now())
            .inputDescription("to be nulled")
            .build()

        StepVerifier.create(repo.insertMeasurement(measurement)).verifyComplete()

        // when
        StepVerifier.create(
            repo.updateMeasurement(
                measurement.id, CarbonMeasurement.Builder()
                    .id(measurement.id)
                    .co2Kg(measurement.co2Kg)
                    .dt(measurement.dt)
                    .inputDescription(Optional.empty())
                    .build()
            )
        )
            .verifyComplete()
        // then
        StepVerifier.create(repo.getMeasurement(measurement.id))
            .assertNext { measurement ->
                assertThat(measurement.inputDescription).isEmpty()
            }
            .verifyComplete()
    }

    @Test
    fun `should create measurement when upserting, if it doesnt exist`() {
        val measurement = CarbonMeasurement.Builder()
            .id("upserted-measurement-id")
            .co2Kg(3.33)
            .dt(Instant.now().truncatedTo(ChronoUnit.MILLIS))
            .inputDescription("to be nulled")
            .build()
        StepVerifier.create(repo.getMeasurements().collectList())
            .assertNext { it.isEmpty() }
            .verifyComplete()

        // when
        StepVerifier.create(repo.upsertMeasurement(measurement.id, measurement)).verifyComplete()

        // then
        StepVerifier.create(repo.getMeasurement(measurement.id))
            .assertNext {
                assertThat(it).isEqualTo(measurement)
            }
            .verifyComplete()
    }

    @Test
    fun `should update measurement when upserting, if it already exists`() {
        val measurement = CarbonMeasurement.Builder()
            .id("upserted-measurement-id")
            .co2Kg(3.33)
            .dt(Instant.now().truncatedTo(ChronoUnit.MILLIS))
            .inputDescription("to be nulled")
            .build()
        val insertAndList = repo.insertMeasurement(measurement).then(repo.getMeasurements().collectList())
        StepVerifier.create(insertAndList)
            .assertNext {
                assertThat(it).hasSize(1)
                assertThat(it[0]).isEqualTo(measurement)
            }
            .verifyComplete()

        // when
        val updated = CarbonMeasurement.Builder()
            .id(measurement.id)
            .co2Kg(6.66)
            .dt(measurement.dt)
            .inputDescription(Optional.empty())
            .build()

        StepVerifier.create(repo.upsertMeasurement(measurement.id, updated)).verifyComplete()

        // then
        StepVerifier.create(repo.getMeasurements().collectList())
            .assertNext {
                assertThat(it).hasSize(1)
                assertThat(it[0]).isEqualTo(updated)
            }
            .verifyComplete()
    }
}
