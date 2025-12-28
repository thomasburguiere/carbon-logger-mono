package ch.burguiere.carbonlog.carbonlogbackend.webflux

import com.fasterxml.jackson.annotation.JsonInclude
//import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
//import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import tools.jackson.databind.json.JsonMapper

@Configuration
open class JacksonConfig {
    @Bean
    open fun customJsonMapper(): JsonMapper {
        return JsonMapper.builder()
            .build()
    }
}
