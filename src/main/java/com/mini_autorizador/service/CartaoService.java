package com.mini_autorizador.service;

import com.mini_autorizador.domain.Cartao;
import com.mini_autorizador.exception.BalanceNotSufficientException;
import com.mini_autorizador.exception.UnknownEntityException;
import com.mini_autorizador.repository.CartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CartaoService {

    @Autowired
    private CartaoRepository cartaoRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Cria um novo cartão.
     *
     * @param numeroCartao Número do cartão.
     * @param senha        Senha do cartão.
     * @return Cartão criado.
     * @throws IllegalArgumentException se o número do cartão já existir.
     */
    public Cartao criarCartao(String numeroCartao, String senha) {

        if (cartaoRepository.findByNumeroCartao(numeroCartao).isPresent()) {
            throw new IllegalArgumentException("Cartão já existe.");
        }

        Cartao novoCartao = Cartao.builder()
                .numeroCartao(numeroCartao)
                .senha(senha)
                .saldo(new BigDecimal("500")) // Saldo inicial padrão
                .build();

        return cartaoRepository.save(novoCartao);
    }

    /**
     * Consulta o saldo de um cartão pelo número.
     *
     * @param numeroCartao Número do cartão.
     * @return Saldo do cartão.
     * @throws UnknownEntityException se o cartão não for encontrado.
     */
    public BigDecimal consultarSaldo(String numeroCartao) {

        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartao)
                .orElseThrow(() -> new UnknownEntityException(
                        String.format("Cartão com número %s não encontrado.", numeroCartao)));
        return cartao.getSaldo();
    }

    public void realizarTransacao(String numeroCartao, String senha, BigDecimal valor) {

        Cartao cartao = cartaoRepository.findByNumeroCartao(numeroCartao)
                .orElseThrow(() -> new UnknownEntityException("Cartão não encontrado."));

        validarSenha(cartao, senha);
        validarSaldo(cartao, valor);

        // Construindo a query (filtro)
        Query query = new Query();
        query.addCriteria(Criteria.where("numeroCartao").is(numeroCartao)
                .and("senha").is(senha)
                .and("saldo").gte(valor));

        // Construindo atualização
        Update update = new Update();
        update.inc("saldo", valor.negate());

        // Executando findAndModify com as configurações
        Cartao cartaoAtualizado = mongoTemplate.findAndModify(
                query,
                update,
                FindAndModifyOptions.options().returnNew(true),
                Cartao.class
        );

    }

    public void validarSenha(Cartao cartao, String senha) {
        Optional.of(cartao)
                .filter(c -> c.getSenha().equals(senha))
                .orElseThrow(() -> new IllegalArgumentException("Senha inválida."));
    }

    public void validarSaldo(Cartao cartao, BigDecimal valor) {
        Optional.of(cartao)
                .filter(c -> c.getSaldo().compareTo(valor) >= 0)
                .orElseThrow(() -> new BalanceNotSufficientException("Saldo insuficiente."));
    }

}

