package com.mini_autorizador.configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {

        // Criação do codecRegistry com o seu Codec personalizado
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromCodecs(new CartaoCodec()),
                MongoClientSettings.getDefaultCodecRegistry()
        );

        // Configuração do MongoClient com os settings
        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(codecRegistry)
                .applyConnectionString(new com.mongodb.ConnectionString("mongodb://user:password@mongo:27017"))
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        // MongoTemplate com o MongoClient configurado
        SimpleMongoClientDatabaseFactory factory = new SimpleMongoClientDatabaseFactory(mongoClient(), "miniautorizador");
        return new MongoTemplate(factory);
    }

}

