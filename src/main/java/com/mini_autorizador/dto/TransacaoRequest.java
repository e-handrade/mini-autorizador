package com.mini_autorizador.dto;

import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class TransacaoRequest extends CartaoRequest{
    private BigDecimal valor;

    public TransacaoRequest(String numeroCartao, String senha) {
        super(numeroCartao, senha);
    }

    public TransacaoRequest() {
        super();
    }

    public TransacaoRequest(String numeroCartao, String senha, BigDecimal valor) {
        super(numeroCartao, senha);
        this.valor = valor;
    }
}

