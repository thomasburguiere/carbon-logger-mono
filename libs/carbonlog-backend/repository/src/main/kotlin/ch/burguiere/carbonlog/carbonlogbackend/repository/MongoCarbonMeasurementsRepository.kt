package ch.burguiere.carbonlog.carbonlogbackend.repository

import ch.burguiere.carbonlog.base.CarbonMeasurement
import ch.burguiere.carbonlog.carbonlogbackend.repository.CarbonMeasurementsRepository.Fields.CO2KG
import ch.burguiere.carbonlog.carbonlogbackend.repository.CarbonMeasurementsRepository.Fields.ID
import ch.burguiere.carbonlog.carbonlogbackend.repository.CarbonMeasurementsRepository.Fields.INPUT_DESCRIPTION
import ch.burguiere.carbonlog.carbonlogbackend.repository.CarbonMeasurementsRepository.Fields.TIMESTAMP
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
        .find(eq(ID.title, BsonString(id)))
        .toMono()
        .map { it.parseMeasurement() }

    override fun insertMeasurement(measurement: CarbonMeasurement): Mono<Void> = collection
        .insertOne(measurement.toBson())
        .toMono()
        .then()

    override fun deleteMeasurement(id: String): Mono<Void> = collection
        .findOneAndDelete(eq(ID.title, BsonString(id)))
        .toMono()
        .then()
}

private fun BsonDocument.parseMeasurement(): CarbonMeasurement =
    CarbonMeasurement(
        id = this.getString(ID.title).value,
        co2Kg = this.getDouble(CO2KG.title).value,
        dt = Instant.ofEpochMilli(this.getDateTime(TIMESTAMP.title).value),
        inputDescription = when {
            this.containsKey(INPUT_DESCRIPTION.title) -> this.getString(INPUT_DESCRIPTION.title).value
            else -> null
        }
    )

private fun CarbonMeasurement.toBson(): BsonDocument =
    BsonDocument()
        .append(CO2KG.title, BsonDouble(this.co2Kg))
        .append(ID.title, BsonString(this.id))
        .append(TIMESTAMP.title, BsonDateTime(this.dt.toEpochMilli()))
        .also { bson ->
            if (this.inputDescription != null) {
                bson.append(INPUT_DESCRIPTION.title, BsonString(this.inputDescription))
            }
        }
