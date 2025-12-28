package ch.burguiere.carbonlog.carbonlogbackend.webflux

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule

@Configuration
open class JacksonConfig {
    @Bean
    open fun customJsonMapper(): JsonMapper {
        return JsonMapper.builder()
            .addModule(KotlinModule.Builder().build())
            .build()
    }
}
