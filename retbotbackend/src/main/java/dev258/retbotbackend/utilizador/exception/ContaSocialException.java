package dev258.retbotbackend.utilizador.exception;

import org.springframework.http.HttpStatus;

public class ContaSocialException extends RuntimeException {

    private final HttpStatus status;

    public ContaSocialException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public ContaSocialException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}