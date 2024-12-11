package com.mini_autorizador.configuration;

import com.mini_autorizador.domain.Cartao;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.*;

import java.math.BigDecimal;

public class CartaoCodec implements Codec<Cartao> {

    @Override
    public Cartao decode(BsonReader reader, DecoderContext decoderContext) {

        reader.readStartDocument();
        String id = reader.readString("id");
        String numeroCartao = reader.readString("numeroCartao");
        BigDecimal saldo = new BigDecimal(reader.readString("saldo"));
        reader.readEndDocument();

        return new Cartao(id, numeroCartao, saldo);
    }

    @Override
    public void encode(BsonWriter writer, Cartao value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("id", value.getId());
        writer.writeString("numeroCartao", value.getNumeroCartao());
        writer.writeString("saldo", value.getSaldo().toString());
        writer.writeEndDocument();
    }

    @Override
    public Class<Cartao> getEncoderClass() {
        return Cartao.class;
    }

}
