package com.mini_autorizador.service;


import com.mini_autorizador.domain.Cartao;
import com.mini_autorizador.exception.BalanceNotSufficientException;
import com.mini_autorizador.exception.UnknownEntityException;
import com.mini_autorizador.repository.CartaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartaoServiceTest {

    @Mock
    private CartaoRepository cartaoRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private CartaoService cartaoService;

    private Cartao cartao;

    @BeforeEach
    public void setUp() {

        MockitoAnnotations.openMocks(this);

        cartao = Cartao.builder()
                .numeroCartao("123456789")
                .senha("senha123")
                .saldo(new BigDecimal("500"))
                .build();
    }

    @Test
    public void testCriarCartaoSuccess() {

        when(cartaoRepository.findByNumeroCartao(cartao.getNumeroCartao())).thenReturn(Optional.empty());
        when(cartaoRepository.save(any(Cartao.class))).thenReturn(cartao);

        Cartao resultado = cartaoService.criarCartao(cartao.getNumeroCartao(), cartao.getSenha());

        assertNotNull(resultado);
        assertEquals(cartao.getNumeroCartao(), resultado.getNumeroCartao());
        assertEquals(cartao.getSenha(), resultado.getSenha());
        assertEquals(cartao.getSaldo(), resultado.getSaldo());

        verify(cartaoRepository, times(1)).save(any(Cartao.class));

    }

    @Test
    public void testCriarCartaoJaExistente() {

        when(cartaoRepository.findByNumeroCartao(cartao.getNumeroCartao())).thenReturn(Optional.of(cartao));

        assertThrows(IllegalArgumentException.class, () ->
                cartaoService.criarCartao(cartao.getNumeroCartao(), cartao.getSenha())
        );

        verify(cartaoRepository, never()).save(any(Cartao.class));
    }

    @Test
    public void testRealizarTransacao_Sucesso() {
        // Mockando as dependências
        when(cartaoRepository.findByNumeroCartao(cartao.getNumeroCartao())).thenReturn(Optional.of(cartao));

        // Mockando a chamada do MongoTemplate
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class), any(), eq(Cartao.class)))
                .thenReturn(cartao);

        // Chamando o método de serviço
        assertDoesNotThrow(() -> cartaoService.realizarTransacao(cartao.getNumeroCartao(), cartao.getSenha(), cartao.getSaldo()));

    }

    @Test
    public void testRealizarTransacao_CartaoNaoEncontrado() {
        when(cartaoRepository.findByNumeroCartao(cartao.getNumeroCartao())).thenReturn(Optional.empty());

        // Verifica se a exceção é lançada quando o cartão não for encontrado
        Exception exception = assertThrows(UnknownEntityException.class, () ->
                cartaoService.realizarTransacao(cartao.getNumeroCartao(), cartao.getSenha(), cartao.getSaldo())
        );
        assertEquals("Cartão não encontrado.", exception.getMessage());
    }

    @Test
    public void testValidarSenha_SenhaInvalida() {
        // Teste para validar a senha
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                cartaoService.validarSenha(cartao, "senhaErrada")
        );
        assertEquals("Senha inválida.", exception.getMessage());
    }

    @Test
    public void testValidarSaldo_SaldoInsuficiente() {
        // Teste para validar saldo insuficiente
        Exception exception = assertThrows(BalanceNotSufficientException.class, () ->
                cartaoService.validarSaldo(cartao, new BigDecimal("1000"))
        );
        assertEquals("Saldo insuficiente.", exception.getMessage());
    }

}
