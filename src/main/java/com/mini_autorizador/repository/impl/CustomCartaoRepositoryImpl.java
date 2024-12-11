package com.mini_autorizador.repository.impl;

import com.mini_autorizador.domain.Cartao;
import com.mini_autorizador.repository.CustomCartaoRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CustomCartaoRepositoryImpl implements CustomCartaoRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Cartao findAndModify(Bson filter, Bson update, FindOneAndUpdateOptions options) {
        MongoCollection<Cartao> collection = mongoTemplate.getDb().getCollection("cartao", Cartao.class);
        return collection.findOneAndUpdate(filter, update, options);
    }

}
