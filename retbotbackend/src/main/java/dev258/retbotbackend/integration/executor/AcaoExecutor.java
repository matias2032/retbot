package dev258.retbotbackend.integration.executor;

import dev258.retbotbackend.utilizador.enums.PlataformaSocial;

/**
 * Contrato que qualquer integração com uma plataforma externa deve cumprir.
 * Módulos de domínio (automacao) dependem apenas desta interface — nunca
 * de uma implementação concreta nem de bibliotecas HTTP/OAuth específicas.
 * Cumpre o Dependency Inversion Principle referido na arquitetura do projeto.
 */
public interface AcaoExecutor {

    /**
     * Indica se esta implementação sabe executar ações para a plataforma dada.
     * Usado pelo ExecutorFactory (ainda por construir) para escolher o
     * executor correto em runtime.
     */
    boolean suporta(PlataformaSocial plataforma);

    /**
     * Executa a ação pedida. Não deve lançar exceções de infraestrutura
     * (timeout, erro HTTP, etc.) — essas devem ser capturadas e traduzidas
     * para um ExecutarAcaoResponse.falha(...), para que o módulo automacao
     * decida o que fazer (retry, marcar como FALHOU) sem apanhar exceções
     * de bibliotecas HTTP.
     */
    ExecutarAcaoResponse executar(ExecutarAcaoRequest pedido);
}