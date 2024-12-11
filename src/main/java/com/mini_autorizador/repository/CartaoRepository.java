package com.mini_autorizador.repository;

import com.mini_autorizador.domain.Cartao;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface CartaoRepository extends MongoRepository<Cartao, String>, CustomCartaoRepository {

    Optional<Cartao> findByNumeroCartao(String numeroCartao);
    Optional<Cartao> findByNumeroCartaoAndSenha(String numeroCartao, String senha);
    boolean existsByNumeroCartao(String numeroCartao);

}

