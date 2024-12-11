package com.mini_autorizador.dto;

import lombok.Data;

@Data
public class CartaoRequest {
    private String numeroCartao;
    private String senha;

    public CartaoRequest(String numeroCartao, String senha) {
        this.numeroCartao = numeroCartao;
        this.senha = senha;
    }

    public CartaoRequest() {

    }
}
