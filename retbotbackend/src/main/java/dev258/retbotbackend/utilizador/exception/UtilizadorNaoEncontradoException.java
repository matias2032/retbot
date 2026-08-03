package dev258.retbotbackend.utilizador.exception;

public class UtilizadorNaoEncontradoException extends RuntimeException {

    public UtilizadorNaoEncontradoException(Long idUtilizador) {
        super("Utilizador não encontrado com id: " + idUtilizador);
    }

    public UtilizadorNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}