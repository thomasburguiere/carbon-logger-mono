package ch.burguiere.carbonlog.carbonlogbackend.repository

import ch.burguiere.carbonlog.base.CarbonMeasurement
import com.mongodb.reactivestreams.client.MongoCollection
import org.bson.BsonDocument
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import java.time.Instant

interface CarbonLogRepository {
    fun getMeasurements(): Flux<CarbonMeasurement>
}

class MongoCarbonLogRepository(val collection: MongoCollection<BsonDocument>) : CarbonLogRepository {
    override fun getMeasurements(): Flux<CarbonMeasurement> {
        return collection.find().toFlux().map { it.parseMeasurement() }
    }
}


private fun BsonDocument.parseMeasurement(): CarbonMeasurement =
    CarbonMeasurement(
        this.getDouble("co2Kg").value,
        Instant.ofEpochMilli(this.getDateTime("timestamp").value)
    )
