package com.mini_autorizador.controller;

import com.mini_autorizador.domain.Cartao;
import com.mini_autorizador.dto.CartaoRequest;
import com.mini_autorizador.dto.TransacaoRequest;
import com.mini_autorizador.exception.BalanceNotSufficientException;
import com.mini_autorizador.exception.UnknownEntityException;
import com.mini_autorizador.service.CartaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {
    @Autowired
    private final CartaoService cartaoService;

    public CartaoController(CartaoService cartaoService) {
        this.cartaoService = cartaoService;
    }

    /**
     * Endpoint para criar um novo cartão.
     *
     * @param cartaoRequest Dados do cartão a ser criado.
     * @return O cartão criado.
     */
    @Operation(summary = "Criar Cartão",
            description = "Criação de um novo cartão")
    @ApiResponse(responseCode = "201", description = "Criação com sucesso")
    @ApiResponse(responseCode = "422", description = "Cartão já existe")
    @ApiResponse(responseCode = "401", description = "Erro na autenticação")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<CartaoRequest> criarCartao(@RequestBody CartaoRequest cartaoRequest) {

        try {

            Cartao novoCartao = cartaoService.criarCartao(cartaoRequest.getNumeroCartao(), cartaoRequest.getSenha());

            return ResponseEntity.status(HttpStatus.CREATED).body(cartaoRequest);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartaoRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(cartaoRequest);
        }

    }

    /**
     * Endpoint para consultar o saldo de um cartão.
     *
     * @param numeroCartao O número do cartão.
     * @return O saldo do cartão.
     */
    @Operation(summary = "Consultar saldo do cartão",
            description = "Consulta o saldo disponível no cartão.")
    @ApiResponse(responseCode = "200", description = "Saldo retornado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
    @ApiResponse(responseCode = "401", description = "Erro an autenticação")
    @GetMapping("/{numeroCartao}/saldo")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BigDecimal> consultarSaldo(@PathVariable String numeroCartao) {

        try {
            BigDecimal saldo = cartaoService.consultarSaldo(numeroCartao);
            return ResponseEntity.ok(saldo);
        } catch (UnknownEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    /**
     * Endpoint para realizar uma transação.
     *
     * @param transacaoRequest numero do cartao, senha e valor
     * @return Mensagem de confirmação.
     */
    @Operation(summary = "Realizar Transação",
            description = "Realiza transações no cartão")
    @ApiResponse(responseCode = "422", description = "Não foi possível concluir a transação")
    @ApiResponse(responseCode = "201", description = "Transação realizada com sucesso")
    @ApiResponse(responseCode = "401", description = "Erro an autenticação")
    @PostMapping("/transacoes")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> realizarTransacao(@RequestBody TransacaoRequest transacaoRequest) {

        try {

            cartaoService.realizarTransacao(
                    transacaoRequest.getNumeroCartao(),
                    transacaoRequest.getSenha(),
                    transacaoRequest.getValor());

            return ResponseEntity.status(201).body("OK");

        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("SENHA_INVALIDA");
        } catch (UnknownEntityException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("CARTAO_INEXISTENTE");
        } catch (BalanceNotSufficientException re) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("SALDO_INSUFICIENTE");
        } catch (Exception re) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERRO NÃO MAPEADO");
        }


    }
}