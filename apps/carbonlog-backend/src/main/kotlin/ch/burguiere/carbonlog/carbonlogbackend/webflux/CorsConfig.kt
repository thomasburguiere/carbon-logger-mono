package ch.burguiere.carbonlog.carbonlogbackend.webflux

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class CorsConfig(@Value("\${cors.allowed.origins}") private val allowedOrigins: List<String>) : WebFluxConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins(*(allowedOrigins.toTypedArray()))
            .allowedMethods("*")
    }
}
