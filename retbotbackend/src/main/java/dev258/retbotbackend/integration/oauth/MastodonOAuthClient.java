package dev258.retbotbackend.integration.oauth;

import dev258.retbotbackend.integration.client.ApiClient;
import dev258.retbotbackend.utilizador.enums.PlataformaSocial;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@RequiredArgsConstructor
public class MastodonOAuthClient implements OAuthClient {

    private static final String SCOPES = "read write follow";

    private final ApiClient apiClient;

    /**
     * Cache em memória de credenciais de app por instância — perde-se ao
     * reiniciar a aplicação. Persistir numa tabela fica pendente.
     */
    private final ConcurrentMap<String, RegistoAplicacaoResponse> appsRegistadas = new ConcurrentHashMap<>();

    @Override
    public boolean suporta(PlataformaSocial plataforma) {
        return plataforma == PlataformaSocial.MASTODON;
    }

    @Override
    public RegistoAplicacaoResponse registrarAplicacao(String urlInstancia, String redirectUri) {
        return appsRegistadas.computeIfAbsent(urlInstancia, instancia -> {
            MultiValueMap<String, String> corpo = new LinkedMultiValueMap<>();
            corpo.add("client_name", "RetbotBackend");
            corpo.add("redirect_uris", redirectUri);
            corpo.add("scopes", SCOPES);
            corpo.add("website", "https://github.com/Dev258");

            MastodonAppRegistroResponse resposta = apiClient.postForm(
                    instancia + "/api/v1/apps", corpo, MastodonAppRegistroResponse.class);

            return new RegistoAplicacaoResponse(resposta.clientId(), resposta.clientSecret());
        });
    }

    @Override
    public String construirUrlAutorizacao(String urlInstancia, String clientId, String redirectUri, String state) {
        return UriComponentsBuilder.fromUriString(urlInstancia + "/oauth/authorize")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", SCOPES)
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    @Override
    public TokenOAuthResponse trocarCodigoPorToken(String urlInstancia, String clientId, String clientSecret,
                                                     String redirectUri, String code) {
        MultiValueMap<String, String> corpo = new LinkedMultiValueMap<>();
        corpo.add("client_id", clientId);
        corpo.add("client_secret", clientSecret);
        corpo.add("redirect_uri", redirectUri);
        corpo.add("grant_type", "authorization_code");
        corpo.add("code", code);
        corpo.add("scope", SCOPES);

        MastodonTokenResponse resposta = apiClient.postForm(
                urlInstancia + "/oauth/token", corpo, MastodonTokenResponse.class);

        return new TokenOAuthResponse(resposta.accessToken(), resposta.tokenType(), resposta.scope());
    }

    // ---- Records internos, só para desserializar o JSON específico do Mastodon ----

    private record MastodonAppRegistroResponse(String clientId, String clientSecret) {}

    private record MastodonTokenResponse(String accessToken, String tokenType, String scope) {}

    @Override
    public PerfilExternoOAuth obterPerfil(String urlInstancia, String accessToken) {
        MastodonPerfilResponse resposta = apiClient.get(
                urlInstancia + "/api/v1/accounts/verify_credentials",
                accessToken,
                MastodonPerfilResponse.class);

        return new PerfilExternoOAuth(resposta.id(), resposta.username(), resposta.displayName());
    }

    private record MastodonPerfilResponse(String id, String username, String displayName) {}
}