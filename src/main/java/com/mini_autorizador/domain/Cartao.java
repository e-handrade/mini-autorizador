package com.mini_autorizador.domain;

import lombok.*;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.mapping.FieldType.DECIMAL128;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "cartoes")
@BsonDiscriminator
public class Cartao {

    @Id
    private String id;

    @BsonProperty("numeroCartao")
    private String numeroCartao;

    @BsonProperty("senha")
    private String senha;

    @BsonProperty("saldo")
    @Field(targetType = DECIMAL128)
    private BigDecimal saldo;

    private String status;

    @Version
    private Long version; // Campo de versão

    public Cartao(String id, String numeroCartao, BigDecimal saldo) {
        this.id = id;
        this.numeroCartao = numeroCartao;
        this.saldo = saldo;
    }
}

