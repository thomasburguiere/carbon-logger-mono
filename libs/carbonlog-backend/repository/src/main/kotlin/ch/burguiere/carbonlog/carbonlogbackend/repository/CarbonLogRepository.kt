package ch.burguiere.carbonlog.carbonlogbackend.repository

import ch.burguiere.carbonlog.base.CarbonMeasurement
import com.mongodb.reactivestreams.client.MongoCollection
import org.bson.BsonDateTime
import org.bson.BsonDocument
import org.bson.BsonDouble
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.time.Instant

interface CarbonLogRepository {
    fun getMeasurements(): Flux<CarbonMeasurement>
    fun insertMeasurement(measurement: CarbonMeasurement): Mono<Void>
}

class MongoCarbonLogRepository(private val collection: MongoCollection<BsonDocument>) : CarbonLogRepository {
    override fun getMeasurements(): Flux<CarbonMeasurement> =
        collection.find().toFlux().map { it.parseMeasurement() }

    override fun insertMeasurement(measurement: CarbonMeasurement): Mono<Void> =
        collection.insertOne(measurement.toBson()).toMono().then()
}


private fun BsonDocument.parseMeasurement(): CarbonMeasurement =
    CarbonMeasurement(
        this.getDouble("co2Kg").value,
        Instant.ofEpochMilli(this.getDateTime("timestamp").value)
    )

private fun CarbonMeasurement.toBson(): BsonDocument =
    BsonDocument()
        .append("co2Kg", BsonDouble(this.co2Kg))
        .append("timestamp", BsonDateTime(this.dt.toEpochMilli()))
