package dev258.retbotbackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Lê o header Authorization, valida o access token e, se válido,
 * autentica o pedido colocando o idUtilizador no SecurityContext.
 * Tokens ausentes/expirados/inválidos simplesmente não autenticam
 * o pedido — quem decide se isso é aceitável é o SecurityConfig
 * (endpoints públicos vs. authenticated()).
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final String PREFIXO_BEARER = "Bearer ";

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith(PREFIXO_BEARER)) {
            String token = header.substring(PREFIXO_BEARER.length());

            try {
                String tipo = jwtService.extrairTipo(token);

                if ("access".equals(tipo)) {
                    Long idUtilizador = jwtService.extrairIdUtilizador(token);

                    var authentication = new UsernamePasswordAuthenticationToken(
                            idUtilizador, null, List.of());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                // type == "refresh" usado num endpoint que não seja /auth/refresh:
                // ignorado deliberadamente — não autentica nada.

            } catch (TokenInvalidoException e) {
                // Token presente mas inválido/expirado: não autentica.
                // O SecurityConfig é que decide se o endpoint exige autenticação.
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}