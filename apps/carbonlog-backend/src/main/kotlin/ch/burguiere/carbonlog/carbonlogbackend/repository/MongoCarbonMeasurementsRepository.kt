package ch.burguiere.carbonlog.carbonlogbackend.repository

import ch.burguiere.carbonlog.model.CarbonMeasurement
import ch.burguiere.carbonlog.carbonlogbackend.repository.MongoCarbonMeasurementsRepository.Fields.CO2_KG
import ch.burguiere.carbonlog.carbonlogbackend.repository.MongoCarbonMeasurementsRepository.Fields.ID
import ch.burguiere.carbonlog.carbonlogbackend.repository.MongoCarbonMeasurementsRepository.Fields.INPUT_DESCRIPTION
import ch.burguiere.carbonlog.carbonlogbackend.repository.MongoCarbonMeasurementsRepository.Fields.TIMESTAMP
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates.combine
import com.mongodb.client.model.Updates.set
import com.mongodb.reactivestreams.client.MongoCollection
import org.bson.BsonDateTime
import org.bson.BsonDocument
import org.bson.BsonDouble
import org.bson.BsonNull
import org.bson.BsonString
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.time.Instant

class MongoCarbonMeasurementsRepository(private val collection: MongoCollection<BsonDocument>) :
    CarbonMeasurementsRepository {

    companion object {
        @JvmStatic
        val collectionName = "Measurements"
    }

    object Fields {
        const val ID = "id"
        const val CO2_KG = "co2Kg"
        const val TIMESTAMP = "timestamp"
        const val INPUT_DESCRIPTION = "inputDescription"
    }

    override fun getMeasurements(): Flux<CarbonMeasurement> =
        collection.find().toFlux().map(BsonDocument::parseMeasurement)

    override fun getMeasurement(id: String): Mono<CarbonMeasurement> = collection
        .find(eq(ID, BsonString(id)))
        .toMono()
        .map(BsonDocument::parseMeasurement)

    override fun insertMeasurement(measurement: CarbonMeasurement): Mono<Void> = collection
        .insertOne(measurement.toBson())
        .toMono()
        .then()

    override fun deleteMeasurement(id: String): Mono<Void> = collection
        .findOneAndDelete(eq(ID, BsonString(id)))
        .toMono()
        .then()

    override fun updateMeasurement(id: String, measurement: CarbonMeasurement): Mono<Void> =
        collection.findOneAndUpdate(
            eq(ID, BsonString(id)),
            combine(
                set(CO2_KG, BsonDouble(measurement.co2Kg)),
                set(TIMESTAMP, BsonDateTime(measurement.dt.toEpochMilli())),
                when (measurement.inputDescription != null) {
                    true -> set(INPUT_DESCRIPTION, BsonString(measurement.inputDescription))
                    else -> set(INPUT_DESCRIPTION, BsonNull())
                },
            )
        )
            .toMono().then()

    fun exists(measurementId: String): Mono<Boolean> =
        collection.find(eq(ID, BsonString(measurementId)))
            .toMono()
            .map { true }
            .defaultIfEmpty(false)

    override fun upsertMeasurement(id: String, measurement: CarbonMeasurement): Mono<Void> =
        exists(measurementId = id)
            .flatMap { exists ->
                when {
                    exists -> updateMeasurement(id, measurement)
                    else -> insertMeasurement(measurement)
                }
            }
}

private fun BsonDocument.parseMeasurement(): CarbonMeasurement =
    CarbonMeasurement(
        id = this.getString(ID).value,
        co2Kg = this.getDouble(CO2_KG).value,
        dt = Instant.ofEpochMilli(this.getDateTime(TIMESTAMP).value),
        inputDescription = when {
            this.containsKey(INPUT_DESCRIPTION) && !this.isNull(INPUT_DESCRIPTION) -> this.getString(INPUT_DESCRIPTION).value
            else -> null
        }
    )

private fun CarbonMeasurement.toBson(): BsonDocument =
    BsonDocument()
        .append(CO2_KG, BsonDouble(this.co2Kg))
        .append(ID, BsonString(this.id))
        .append(TIMESTAMP, BsonDateTime(this.dt.toEpochMilli()))
        .also { bson ->
            if (this.inputDescription != null) {
                bson.append(INPUT_DESCRIPTION, BsonString(this.inputDescription))
            }
        }
