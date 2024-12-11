package com.mini_autorizador.exception;

/**
 * Exceção personalizada para tratar casos onde um cartão não é encontrado.
 */
public class UnknownEntityException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnknownEntityException(String message) {
        super(message);
    }
}

