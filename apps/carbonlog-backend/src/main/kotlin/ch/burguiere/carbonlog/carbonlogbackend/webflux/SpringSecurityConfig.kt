package ch.burguiere.carbonlog.carbonlogbackend.webflux

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class StaticBearerTokenFilter(val staticToken: String) : WebFilter {
    private val publicPaths: List<String> = listOf("/healthz", "/actuator/health")

    private fun isAuthed(exchange: ServerWebExchange): Boolean =
        exchange.request.headers.getFirst("Authorization")
            .let { authHeader ->
                authHeader != null
                        && authHeader.startsWith("Bearer")
                        && authHeader.split(" ").let { !it.isEmpty() && it.last() == staticToken }
            }

    private fun isPublicUrl(exchange: ServerWebExchange): Boolean =
        publicPaths.contains(exchange.request.uri.path)

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void> {
        if (isPublicUrl(exchange)) {
            return chain.filter(exchange)
        }
        if (!isAuthed(exchange)) {
            exchange.response.statusCode = UNAUTHORIZED
            return exchange.response.setComplete()
        }
        val auth: Authentication = UsernamePasswordAuthenticationToken(
            "staticUser",
            "N/A",
            mutableListOf<GrantedAuthority>()
        )
        return chain.filter(exchange)
            .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
    }
}

@EnableWebFluxSecurity
@Configuration
open class SpringSecurityConfig(
    @param:Value($$"${static.auth.token}") private val staticToken: String
) {

    @Bean
    open fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain = http
        .addFilterAt(StaticBearerTokenFilter(staticToken), SecurityWebFiltersOrder.AUTHENTICATION)
        .authorizeExchange { exchange: ServerHttpSecurity.AuthorizeExchangeSpec ->
            exchange
                .pathMatchers(GET, "/healthz").permitAll()
                .pathMatchers(GET, "/actuator/**").permitAll()
                .pathMatchers("/carbon-logs/**").authenticated()
        }
        .csrf { it.disable() }
        .build()
}
