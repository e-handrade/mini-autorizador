package com.mini_autorizador.controller;

import com.mini_autorizador.domain.Cartao;
import com.mini_autorizador.dto.CartaoRequest;
import com.mini_autorizador.dto.TransacaoRequest;
import com.mini_autorizador.exception.BalanceNotSufficientException;
import com.mini_autorizador.exception.UnknownEntityException;
import com.mini_autorizador.service.CartaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartaoControllerTest {

    @Mock
    private CartaoService cartaoService;

    @InjectMocks
    private CartaoController cartaoController;

    private CartaoRequest cartaoRequest;

    private Cartao cartao;

    @BeforeEach
    public void setUp() {

        // Setup para as instâncias necessárias para os testes
        cartaoRequest = new CartaoRequest("123456789", "senha123");

        // Mockando o comportamento do serviço
        cartao = Cartao.builder()
                .numeroCartao("123456789")
                .senha("senha123")
                .saldo(new BigDecimal("500"))
                .build();

    }

    @Test
    public void testCriarCartaoSuccess() {

        when(cartaoService.criarCartao(cartaoRequest.getNumeroCartao(), cartaoRequest.getSenha())).thenReturn(cartao);

        // Chamando o método do controller
        ResponseEntity<CartaoRequest> response = cartaoController.criarCartao(cartaoRequest);

        // Verificando o código de status
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCriarCartaoCartaoJaExistente() {

        // Mockando o comportamento do serviço para lançar IllegalArgumentException
        when(cartaoService.criarCartao(cartaoRequest.getNumeroCartao(), cartaoRequest.getSenha()))
                .thenThrow(new IllegalArgumentException("Cartão já existe"));

        // Chamando o método do controller
        ResponseEntity<CartaoRequest> response = cartaoController.criarCartao(cartaoRequest);

        // Verificando o código de status
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

    @Test
    public void testConsultarSaldoSuccess() {

        // Mockando o comportamento do serviço
        BigDecimal saldo = BigDecimal.valueOf(500);
        when(cartaoService.consultarSaldo(cartaoRequest.getNumeroCartao())).thenReturn(saldo);

        // Chamando o método do controller
        ResponseEntity<BigDecimal> response = cartaoController.consultarSaldo(cartaoRequest.getNumeroCartao());

        // Verificando o código de status e o valor do saldo
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(saldo, response.getBody());
    }

    @Test
    public void testConsultarSaldoCartaoNaoEncontrado() {

        // Mockando o comportamento do serviço para lançar UnknownEntityException
        when(cartaoService.consultarSaldo(cartaoRequest.getNumeroCartao()))
                .thenThrow(new UnknownEntityException("Cartão não encontrado"));

        // Chamando o método do controller
        ResponseEntity<BigDecimal> response = cartaoController.consultarSaldo(cartaoRequest.getNumeroCartao());

        // Verificando o código de status
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRealizarTransacaoSuccess() {

        // Mockando o comportamento do serviço
        doNothing().when(cartaoService).realizarTransacao(anyString(), anyString(), any(BigDecimal.class));

        // Criando um request de transação
        TransacaoRequest transacaoRequest = new TransacaoRequest("123456789", "senha123", BigDecimal.valueOf(100));

        // Chamando o método do controller
        ResponseEntity<String> response = cartaoController.realizarTransacao(transacaoRequest);

        // Verificando o código de status
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("OK", response.getBody());
    }

    @Test
    public void testRealizarTransacaoSaldoInsuficiente() {

        // Mockando o comportamento do serviço para lançar RuntimeException
        doThrow(new BalanceNotSufficientException("Saldo insuficiente")).when(cartaoService)
                .realizarTransacao(anyString(), anyString(), any(BigDecimal.class));

        // Criando um request de transação
        TransacaoRequest transacaoRequest = new TransacaoRequest("123456789", "senha123", BigDecimal.valueOf(100));

        // Chamando o método do controller
        ResponseEntity<String> response = cartaoController.realizarTransacao(transacaoRequest);

        // Verificando o código de status
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("SALDO_INSUFICIENTE", response.getBody());

    }

    @Test
    public void testRealizarTransacaoCartaoInexistente() {

        // Mockando o comportamento do serviço para lançar UnknownEntityException
        doThrow(new UnknownEntityException("Cartão não encontrado")).when(cartaoService)
                .realizarTransacao(anyString(), anyString(), any(BigDecimal.class));

        // Criando um request de transação
        TransacaoRequest transacaoRequest = new TransacaoRequest("123456789", "senha123", BigDecimal.valueOf(100));

        // Chamando o método do controller
        ResponseEntity<String> response = cartaoController.realizarTransacao(transacaoRequest);

        // Verificando o código de status
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("CARTAO_INEXISTENTE", response.getBody());

    }

    @Test
    public void testRealizarTransacaoSenhaInvalida() {

        // Mockando o comportamento do serviço para lançar IllegalArgumentException
        doThrow(new IllegalArgumentException("Senha inválida")).when(cartaoService)
                .realizarTransacao(anyString(), anyString(), any(BigDecimal.class));

        // Criando um request de transação
        TransacaoRequest transacaoRequest = new TransacaoRequest("123456789", "senha123", BigDecimal.valueOf(100));

        // Chamando o método do controller
        ResponseEntity<String> response = cartaoController.realizarTransacao(transacaoRequest);

        // Verificando o código de status
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("SENHA_INVALIDA", response.getBody());

    }
}