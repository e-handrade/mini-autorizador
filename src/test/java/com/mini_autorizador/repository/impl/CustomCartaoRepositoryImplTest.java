package com.mini_autorizador.repository.impl;

import com.mini_autorizador.domain.Cartao;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.conversions.Bson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomCartaoRepositoryImplTest {

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private MongoDatabase mongoDatabase;

    @Mock
    private MongoCollection<Cartao> collectionMock;

    @InjectMocks
    private CustomCartaoRepositoryImpl customCartaoRepository;

    @Test
    public void testFindAndModifySuccess() {
        // Arrange
        Cartao cartaoMock = new Cartao();
        cartaoMock.setNumeroCartao("123456");

        // Mocking the behavior of mongoTemplate, mongoDatabase, and MongoCollection
        when(mongoTemplate.getDb()).thenReturn(mongoDatabase);
        when(mongoDatabase.getCollection("cartao", Cartao.class)).thenReturn(collectionMock);
        when(collectionMock.findOneAndUpdate(any(Bson.class), any(Bson.class), any())).thenReturn(cartaoMock);

        // Act
        Cartao result = customCartaoRepository.findAndModify(
                Filters.eq("numeroCartao", "123456"),
                Updates.combine(Updates.set("saldo", 100.0)),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
        );

        // Assert
        assertNotNull(result);
        assertEquals("123456", result.getNumeroCartao());
        verify(collectionMock, times(1)).findOneAndUpdate(any(Bson.class), any(Bson.class), any());
    }

}
