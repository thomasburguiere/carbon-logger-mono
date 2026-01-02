package ch.burguiere.carbonlog.carbonlogbackend.repository

import ch.burguiere.carbonlog.base.CarbonMeasurement
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


    override fun getMeasurements(): Flux<CarbonMeasurement> =
        collection.find().toFlux().map { it.parseMeasurement() }

    override fun getMeasurement(id: String): Mono<CarbonMeasurement> = collection
        .find(eq(Fields.ID.name, BsonString(id)))
        .toMono()
        .map { it.parseMeasurement() }

    override fun insertMeasurement(measurement: CarbonMeasurement): Mono<Void> = collection
        .insertOne(measurement.toBson())
        .toMono()
        .then()
}

private fun BsonDocument.parseMeasurement(): CarbonMeasurement =
    CarbonMeasurement(
        id = this.getString(Fields.ID.name).value,
        co2Kg = this.getDouble(Fields.CO2KG.name).value,
        dt = Instant.ofEpochMilli(this.getDateTime("timestamp").value),
        inputDescription = when {
            this.containsKey("inputDescription") -> this.getString("inputDescription").value
            else -> null
        }
    )

private fun CarbonMeasurement.toBson(): BsonDocument =
    BsonDocument()
        .append(Fields.CO2KG.name, BsonDouble(this.co2Kg))
        .append(Fields.ID.name, BsonString(this.id))
        .append("timestamp", BsonDateTime(this.dt.toEpochMilli()))
        .also { bson ->
            if (this.inputDescription != null) {
                bson.append("inputDescription", BsonString(this.inputDescription))
            }
        }
