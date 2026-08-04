package dev258.retbotbackend.integration.executor;

/**
 * Resposta genérica de um AcaoExecutor, independente da plataforma.
 * Serve de base para o registo em Execucao (módulo automacao).
 */
public record ExecutarAcaoResponse(
        boolean sucesso,
        Integer codigoHttp,
        String idExterno,
        String mensagem,
        String requestId
) {
    public static ExecutarAcaoResponse sucesso(Integer codigoHttp, String idExterno, String requestId) {
        return new ExecutarAcaoResponse(true, codigoHttp, idExterno, null, requestId);
    }

    public static ExecutarAcaoResponse falha(Integer codigoHttp, String mensagem, String requestId) {
        return new ExecutarAcaoResponse(false, codigoHttp, null, mensagem, requestId);
    }
}