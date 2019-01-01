package com.example.demo.utils;

import com.example.demo.model.RankedUrl;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class RankedUrlSerializer extends StdSerializer<RankedUrl> {
    public RankedUrlSerializer() {
        super(RankedUrl.class);
    }

    public RankedUrlSerializer(Class<RankedUrl> type) {
        super(type);
    }

    @Override
    public void serialize(RankedUrl value, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        generator.writeStartObject();
        generator.writeStringField("link", "/l/" + Base62.to(value.getId()));
        generator.writeStringField("original", value.getOriginal());
        generator.writeNumberField("rank", value.getRank());
        generator.writeNumberField("count", value.getRedirects());
        generator.writeEndObject();
    }
}
