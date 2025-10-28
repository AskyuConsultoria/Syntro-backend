package consultoria.askyu.syntro.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
open class SecurityConfig(
    private val corsConfigurationSource: CorsConfigurationSource
) {

    @Bean
    open fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.configurationSource(corsConfigurationSource) } // Usa o bean diretamente
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/syntro/**").permitAll()
                    .anyRequest().permitAll()
            }

        return http.build()
    }
}
