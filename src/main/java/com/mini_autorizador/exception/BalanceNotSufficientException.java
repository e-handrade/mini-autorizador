package com.mini_autorizador.exception;
/**
 * Exceção personalizada para tratar casos onde saldo é insuficiente.
 */
public class BalanceNotSufficientException extends RuntimeException{
    public BalanceNotSufficientException(String message) {
        super(message);
    }
}
