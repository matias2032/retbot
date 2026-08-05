package dev258.retbotbackend.security;

import dev258.retbotbackend.security.dto.LoginRequest;
import dev258.retbotbackend.security.dto.LoginResponse;
import dev258.retbotbackend.security.dto.TokensEmitidos;
import dev258.retbotbackend.utilizador.dto.UtilizadorResponse;
import dev258.retbotbackend.utilizador.entity.Utilizador;
import dev258.retbotbackend.utilizador.service.UtilizadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String NOME_COOKIE_REFRESH = "refresh_token";
    private static final String PATH_COOKIE_REFRESH = "/api/v1/auth";

    private final AuthService authService;
    private final JwtService jwtService;
    private final UtilizadorService utilizadorService;

    @Value("${app.security.cookie-secure}")
    private boolean cookieSecure;

    @GetMapping("/me")
    public ResponseEntity<UtilizadorResponse> me(@AuthenticationPrincipal Long idUtilizador) {
        Utilizador utilizador = utilizadorService.buscarUtilizador(idUtilizador);
        return ResponseEntity.ok(UtilizadorResponse.from(utilizador));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        TokensEmitidos tokens = authService.login(request.email(), request.senha());
        return responderComCookie(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @CookieValue(name = NOME_COOKIE_REFRESH, required = false) String refreshToken) {

        if (refreshToken == null) {
            throw new TokenInvalidoException("Refresh token não fornecido");
        }

        TokensEmitidos tokens = authService.refresh(refreshToken);
        return responderComCookie(tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookieLimpo = ResponseCookie.from(NOME_COOKIE_REFRESH, "")
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path(PATH_COOKIE_REFRESH)
                .maxAge(0)
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookieLimpo.toString())
                .build();
    }

    private ResponseEntity<LoginResponse> responderComCookie(TokensEmitidos tokens) {
        ResponseCookie cookieRefresh = ResponseCookie.from(NOME_COOKIE_REFRESH, tokens.refreshToken())
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite("Lax")
                .path(PATH_COOKIE_REFRESH)
                .maxAge(jwtService.getExpiracaoRefreshTokenSegundos())
                .build();

        LoginResponse corpo = new LoginResponse(
                tokens.accessToken(), tokens.tipo(), tokens.expiraEmSegundos());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieRefresh.toString())
                .body(corpo);
    }
}