package dev258.retbotbackend.integration.client;

import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.net.http.HttpClient;
import java.time.Duration;

@Component
public class ApiClient {

    private final RestClient restClient;

    public ApiClient() {
        this.restClient = RestClient.builder()
                .requestFactory(new JdkClientHttpRequestFactory(
                        HttpClient.newBuilder()
                                .connectTimeout(Duration.ofSeconds(10))
                                .build()))
                .build();
    }

    public <T> T postJson(String url, Object corpo, String bearerToken, Class<T> tipoResposta) {
        try {
            RestClient.RequestBodySpec pedido = restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_JSON);
            if (bearerToken != null) {
                pedido = pedido.header("Authorization", "Bearer " + bearerToken);
            }
            return pedido.body(corpo).retrieve().body(tipoResposta);
        } catch (RestClientResponseException ex) {
            throw new ApiClienteException(
                    "Erro ao chamar " + url + ": " + ex.getStatusCode(),
                    ex.getStatusCode().value(), ex);
        }
    }

    public <T> T postForm(String url, MultiValueMap<String, String> corpo, Class<T> tipoResposta) {
        try {
            return restClient.post()
                    .uri(url)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(corpo)
                    .retrieve()
                    .body(tipoResposta);
        } catch (RestClientResponseException ex) {
            throw new ApiClienteException(
                    "Erro ao chamar " + url + ": " + ex.getStatusCode(),
                    ex.getStatusCode().value(), ex);
        }
    }

    public <T> T get(String url, String bearerToken, Class<T> tipoResposta) {
        try {
            RestClient.RequestHeadersSpec<?> pedido = restClient.get().uri(url);
            if (bearerToken != null) {
                pedido = pedido.header("Authorization", "Bearer " + bearerToken);
            }
            return pedido.retrieve().body(tipoResposta);
        } catch (RestClientResponseException ex) {
            throw new ApiClienteException(
                    "Erro ao chamar " + url + ": " + ex.getStatusCode(),
                    ex.getStatusCode().value(), ex);
        }
    }
}