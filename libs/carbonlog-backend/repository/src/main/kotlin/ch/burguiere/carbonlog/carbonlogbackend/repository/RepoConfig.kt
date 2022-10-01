package ch.burguiere.carbonlog.carbonlogbackend.repository

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import com.mongodb.reactivestreams.client.MongoDatabase
import org.bson.BsonDocument
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepoConfig(@Value("\${mongourl.measurements.url}")private val connectionString: String) {

    @Bean
    fun mongoDatabase(): MongoDatabase = mongoClient().getDatabase("carbon_measurements")


    @Bean
    fun mongoClient(): MongoClient = MongoClients.create(connectionString)


    @Bean
    fun carbonLogRepository(): CarbonLogRepository =
        MongoCarbonLogRepository(mongoDatabase().getCollection("Measurements", BsonDocument::class.java));

}
