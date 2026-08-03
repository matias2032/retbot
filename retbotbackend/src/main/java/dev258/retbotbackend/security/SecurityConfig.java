package dev258.retbotbackend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // Documentação/infra — sempre públicos.
    private static final String[] ENDPOINTS_DOCUMENTACAO = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/api-docs/**"
    };

    // Autenticação — login e refresh não podem exigir estar autenticado.
    private static final String ENDPOINTS_AUTH = "/api/v1/auth/**";

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Registo: só o POST exato fica público; GET/PUT/DELETE
                        // em /api/v1/utilizadores/** continuam protegidos.
                        .requestMatchers(HttpMethod.POST, "/api/v1/utilizadores").permitAll()
                        .requestMatchers(ENDPOINTS_AUTH).permitAll()
                        .requestMatchers(ENDPOINTS_DOCUMENTACAO).permitAll()
                        // publicacao/** e automacao/** não têm rota pública —
                        // caem naturalmente aqui, exigindo token válido.
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}


