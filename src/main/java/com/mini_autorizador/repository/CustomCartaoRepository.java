package com.mini_autorizador.repository;

import com.mini_autorizador.domain.Cartao;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.conversions.Bson;

public interface CustomCartaoRepository {
    Cartao findAndModify(Bson filter, Bson update, FindOneAndUpdateOptions options);
}
