package ch.burguiere.carbonlog.carbonlogbackend.repository

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.BsonDocument
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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
    open fun carbonLogRepository(): CarbonLogRepository =
        MongoCarbonLogRepository(mongoDatabase().getCollection(measurementsCollectionName, BsonDocument::class.java))

}
