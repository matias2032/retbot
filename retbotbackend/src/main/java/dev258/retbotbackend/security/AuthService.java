package dev258.retbotbackend.security;

import dev258.retbotbackend.security.dto.LoginResponse;
import dev258.retbotbackend.utilizador.entity.Utilizador;
import dev258.retbotbackend.utilizador.repository.UtilizadorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UtilizadorRepository utilizadorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(String email, String senha) {
        Utilizador utilizador = utilizadorRepository.findByEmail(email)
                .orElseThrow(() -> new CredenciaisInvalidasException("Email ou senha inválidos"));

        if (Boolean.FALSE.equals(utilizador.getAtivo())) {
            throw new CredenciaisInvalidasException("Conta desativada");
        }

        if (!passwordEncoder.matches(senha, utilizador.getSenhaHash())) {
            throw new CredenciaisInvalidasException("Email ou senha inválidos");
        }

        return emitirTokens(utilizador);
    }

    public LoginResponse refresh(String refreshToken) {
        Map<String, Object> payload = jwtService.validarToken(refreshToken);

        if (!"refresh".equals(payload.get("type"))) {
            throw new TokenInvalidoException("Token fornecido não é um refresh token");
        }

        Long idUtilizador = Long.valueOf((String) payload.get("sub"));

        Utilizador utilizador = utilizadorRepository.findById(idUtilizador)
                .orElseThrow(() -> new CredenciaisInvalidasException("Utilizador não encontrado"));

        return emitirTokens(utilizador);
    }

    private LoginResponse emitirTokens(Utilizador utilizador) {
        String accessToken = jwtService.gerarAccessToken(utilizador.getIdUtilizador(), utilizador.getEmail());
        String refreshToken = jwtService.gerarRefreshToken(utilizador.getIdUtilizador());

        return new LoginResponse(accessToken, refreshToken, "Bearer", jwtService.getExpiracaoAccessTokenSegundos());
    }
}