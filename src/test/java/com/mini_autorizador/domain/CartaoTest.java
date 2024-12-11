package com.mini_autorizador.domain;

import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CartaoTest {

    @Test
    public void testCartaoCreation() {
        // Dados de entrada
        String numeroCartao = "123456789";
        String senha = "1234";
        BigDecimal saldo = new BigDecimal("500.00");

        // Criando o Cartao
        Cartao cartao = new Cartao("1", numeroCartao, saldo);

        // Verificando se os campos foram inicializados corretamente
        assertEquals(numeroCartao, cartao.getNumeroCartao());
        assertEquals(saldo, cartao.getSaldo());
        assertNull(cartao.getStatus());
        assertNull(cartao.getVersion());
    }

}
