package dev258.retbotbackend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Geração e validação de tokens JWT (HS256) para autenticação na própria API.
 * Implementado apenas com javax.crypto/JDK — não depende de jjwt nem de
 * qualquer outra biblioteca externa de JWT.
 *
 * Importante: isto NÃO é o mesmo tipo de token de conta_social.access_token
 * (esse vem da plataforma externa via OAuth). Este JWT autentica o utilizador
 * perante a nossa própria API.
 */
@Service
public class JwtService {

    private static final String ALGORITMO = "HmacSHA256";
    private static final String HEADER_JSON = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
    private static final Base64.Encoder BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder BASE64_DECODER = Base64.getUrlDecoder();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.jwt.secret}")
    private String segredo;

    @Value("${app.jwt.expiration-ms}")
    private long expiracaoAccessTokenMs;

    @Value("${app.jwt.refresh-expiration-ms}")
    private long expiracaoRefreshTokenMs;

    // ---------- Geração ----------

    public String gerarAccessToken(Long idUtilizador, String email) {
        return gerarToken(idUtilizador, email, "access", expiracaoAccessTokenMs);
    }

    public String gerarRefreshToken(Long idUtilizador) {
        return gerarToken(idUtilizador, null, "refresh", expiracaoRefreshTokenMs);
    }

    private String gerarToken(Long idUtilizador, String email, String tipo, long duracaoMs) {
        Instant agora = Instant.now();

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", String.valueOf(idUtilizador));
        if (email != null) {
            payload.put("email", email);
        }
        payload.put("type", tipo);
        payload.put("iat", agora.getEpochSecond());
        payload.put("exp", agora.plusMillis(duracaoMs).getEpochSecond());

        String headerB64 = BASE64_ENCODER.encodeToString(HEADER_JSON.getBytes(StandardCharsets.UTF_8));
        String payloadB64 = codificarPayload(payload);
        String assinatura = assinar(headerB64 + "." + payloadB64);

        return headerB64 + "." + payloadB64 + "." + assinatura;
    }

    // ---------- Validação ----------

    /**
     * Valida assinatura e expiração. Lança TokenInvalidoException se o token
     * estiver mal formado, com assinatura inválida, ou expirado.
     */
    public Map<String, Object> validarToken(String token) {
        String[] partes = token.split("\\.");
        if (partes.length != 3) {
            throw new TokenInvalidoException("Formato de token inválido");
        }

        String headerB64 = partes[0];
        String payloadB64 = partes[1];
        String assinaturaRecebida = partes[2];

        String assinaturaEsperada = assinar(headerB64 + "." + payloadB64);

        boolean assinaturaValida = MessageDigest.isEqual(
                assinaturaEsperada.getBytes(StandardCharsets.UTF_8),
                assinaturaRecebida.getBytes(StandardCharsets.UTF_8)
        );

        if (!assinaturaValida) {
            throw new TokenInvalidoException("Assinatura do token inválida");
        }

        Map<String, Object> payload = descodificarPayload(payloadB64);

        long exp = ((Number) payload.get("exp")).longValue();
        if (Instant.now().getEpochSecond() > exp) {
            throw new TokenInvalidoException("Token expirado");
        }

        return payload;
    }

    public Long extrairIdUtilizador(String token) {
        Map<String, Object> payload = validarToken(token);
        return Long.valueOf((String) payload.get("sub"));
    }

    public String extrairTipo(String token) {
        Map<String, Object> payload = validarToken(token);
        return (String) payload.get("type");
    }

    // ---------- Internos ----------

    private String codificarPayload(Map<String, Object> payload) {
        try {
            byte[] json = objectMapper.writeValueAsBytes(payload);
            return BASE64_ENCODER.encodeToString(json);
        } catch (Exception e) {
            throw new TokenInvalidoException("Erro ao gerar token: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> descodificarPayload(String payloadB64) {
        try {
            byte[] json = BASE64_DECODER.decode(payloadB64);
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            throw new TokenInvalidoException("Erro ao ler token: " + e.getMessage());
        }
    }

    private String assinar(String dados) {
        try {
            Mac mac = Mac.getInstance(ALGORITMO);
            mac.init(new SecretKeySpec(segredo.getBytes(StandardCharsets.UTF_8), ALGORITMO));
            byte[] assinatura = mac.doFinal(dados.getBytes(StandardCharsets.UTF_8));
            return BASE64_ENCODER.encodeToString(assinatura);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("Erro ao assinar token JWT", e);
        }
    }public long getExpiracaoAccessTokenSegundos() {
    return expiracaoAccessTokenMs / 1000;
}

public long getExpiracaoRefreshTokenSegundos() {
    return expiracaoRefreshTokenMs / 1000;
}

}