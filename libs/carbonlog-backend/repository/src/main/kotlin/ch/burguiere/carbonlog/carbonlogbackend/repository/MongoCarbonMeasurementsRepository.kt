package ch.burguiere.carbonlog.carbonlogbackend.repository

import ch.burguiere.carbonlog.base.CarbonMeasurement
import ch.burguiere.carbonlog.carbonlogbackend.repository.MongoCarbonMeasurementsRepository.Fields
import com.mongodb.client.model.Filters.eq
import com.mongodb.reactivestreams.client.MongoCollection
import org.bson.BsonDateTime
import org.bson.BsonDocument
import org.bson.BsonDouble
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

    enum class Fields {
        id,
        co2Kg,
        timestamp,
        inputDescription,
    }

    override fun getMeasurements(): Flux<CarbonMeasurement> =
        collection.find().toFlux().map(BsonDocument::parseMeasurement)

    override fun getMeasurement(id: String): Mono<CarbonMeasurement> = collection
        .find(eq(Fields.id.name, BsonString(id)))
        .toMono()
        .map(BsonDocument::parseMeasurement)

    override fun insertMeasurement(measurement: CarbonMeasurement): Mono<Void> = collection
        .insertOne(measurement.toBson())
        .toMono()
        .then()

    override fun deleteMeasurement(id: String): Mono<Void> = collection
        .findOneAndDelete(eq(Fields.id.name, BsonString(id)))
        .toMono()
        .then()
}

private fun BsonDocument.parseMeasurement(): CarbonMeasurement =
    CarbonMeasurement(
        id = this.getString(Fields.id.name).value,
        co2Kg = this.getDouble(Fields.co2Kg.name).value,
        dt = Instant.ofEpochMilli(this.getDateTime(Fields.timestamp.name).value),
        inputDescription = when {
            this.containsKey(Fields.inputDescription.name) -> this.getString(Fields.inputDescription.name).value
            else -> null
        }
    )

private fun CarbonMeasurement.toBson(): BsonDocument =
    BsonDocument()
        .append(Fields.co2Kg.name, BsonDouble(this.co2Kg))
        .append(Fields.id.name, BsonString(this.id))
        .append(Fields.timestamp.name, BsonDateTime(this.dt.toEpochMilli()))
        .also { bson ->
            if (this.inputDescription != null) {
                bson.append(Fields.inputDescription.name, BsonString(this.inputDescription))
            }
        }
