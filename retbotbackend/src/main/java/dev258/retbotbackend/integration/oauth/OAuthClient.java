package dev258.retbotbackend.integration.oauth;

import dev258.retbotbackend.utilizador.enums.PlataformaSocial;

/**
 * Contrato para o fluxo OAuth2 de uma plataforma externa. Tal como
 * AcaoExecutor, cada plataforma tem a sua implementação; o chamador
 * (módulo utilizador) nunca fala diretamente com a API OAuth da plataforma.
 */
public interface OAuthClient {

    boolean suporta(PlataformaSocial plataforma);

    /** Regista a aplicação na instância (Mastodon) ou é no-op (plataformas OAuth centralizadas). */
    RegistoAplicacaoResponse registrarAplicacao(String urlInstancia, String redirectUri);

    /** Constrói o URL para onde redirecionar o utilizador para autorizar. */
    String construirUrlAutorizacao(String urlInstancia, String clientId, String redirectUri, String state);

    /** Troca o "code" devolvido no callback por um access_token. */
    TokenOAuthResponse trocarCodigoPorToken(String urlInstancia, String clientId, String clientSecret,
                                             String redirectUri, String code);

                                             /** Obtém a identidade do utilizador na plataforma externa, usando o access_token. */
    PerfilExternoOAuth obterPerfil(String urlInstancia, String accessToken);
}