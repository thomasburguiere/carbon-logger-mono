package ch.burguiere.carbonlog.carbonlogbackend.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.GET
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtValidators
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.util.Assert


@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    @Value("\${auth0.audience}") private val audience: String,
    @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}") private val issuer: String
) {

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain? {
        http
            .authorizeExchange { exchanges: AuthorizeExchangeSpec ->

                exchanges
                    .matchers(ServerWebExchangeMatchers.pathMatchers(GET, "/carbon-logs/measurements/**")).permitAll()
                    .anyExchange().authenticated()
                    .and().oauth2ResourceServer().jwt().jwtDecoder(jwtDecoder())
            }
        return http.build()
    }

    fun jwtDecoder(): ReactiveJwtDecoder? {
        val withAudience: OAuth2TokenValidator<Jwt> = AudienceValidator(audience)
        val withIssuer: OAuth2TokenValidator<Jwt> = JwtValidators.createDefaultWithIssuer(issuer)
        val validator: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withAudience, withIssuer)
        val jwtDecoder: NimbusReactiveJwtDecoder =
            ReactiveJwtDecoders.fromOidcIssuerLocation(issuer) as NimbusReactiveJwtDecoder
        jwtDecoder.setJwtValidator(validator)
        return jwtDecoder
    }
}


internal class AudienceValidator(audience: String) : OAuth2TokenValidator<Jwt> {
    private val audience: String

    init {
        Assert.hasText(audience, "audience is null or empty")
        this.audience = audience
    }

    override fun validate(jwt: Jwt): OAuth2TokenValidatorResult {
        val audiences: List<String> = jwt.audience
        if (audiences.contains(audience)) {
            return OAuth2TokenValidatorResult.success()
        }
        val err = OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN)
        return OAuth2TokenValidatorResult.failure(err)
    }
}
