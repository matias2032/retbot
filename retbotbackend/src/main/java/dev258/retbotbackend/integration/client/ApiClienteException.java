package dev258.retbotbackend.integration.client;

/**
 * Erro ao comunicar com uma API externa (timeout, resposta de erro HTTP, etc).
 * Não é uma exceção de domínio — quem apanha isto decide se traduz para
 * ExecutarAcaoResponse.falha(...) (no caso dos executores) ou deixa
 * propagar até ao GlobalExceptionHandler (no caso do fluxo OAuth).
 */
public class ApiClienteException extends RuntimeException {

    private final Integer statusCode;

    public ApiClienteException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ApiClienteException(String message, Integer statusCode, Throwable causa) {
        super(message, causa);
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }
}