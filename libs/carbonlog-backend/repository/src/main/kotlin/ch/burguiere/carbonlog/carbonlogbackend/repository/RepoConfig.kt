package ch.burguiere.carbonlog.carbonlogbackend.repository

import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes.ascending
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.BsonDocument
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.kotlin.core.publisher.toMono
import java.time.Duration

@Configuration
open class RepoConfig(
    @param:Value("\${mongourl.measurements.url}") private val connectionString: String,
    @param:Value("\${mongourl.measurements.db-name}") private val dbName: String
) {

    @Bean
    open fun mongoDatabase(): MongoDatabase = mongoClient().getDatabase(dbName)


    @Bean
    open fun mongoClient(): MongoClient = MongoClients.create(connectionString)


    @Bean
    open fun carbonLogRepository(): CarbonMeasurementsRepository {
        val collection = mongoDatabase()
            .getCollection(MongoCarbonMeasurementsRepository.collectionName, BsonDocument::class.java)

        collection.createIndex(
            ascending(CarbonMeasurementsRepository.Fields.ID.title),
            IndexOptions().background(true).unique(true)
        ).toMono().block(Duration.ofSeconds(5))
        return MongoCarbonMeasurementsRepository(collection)
    }

}
