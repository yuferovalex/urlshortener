package com.example.demo.utils;

import com.example.demo.RankedUrlImpl;
import com.example.demo.model.RankedUrl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RankedUrlSerializerTest {

    @Test
    public void serialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(RankedUrl.class, new RankedUrlSerializer());
        mapper.registerModule(module);
        final RankedUrl EXPECTED_URL = new RankedUrlImpl(1, 1, 2, "https://kontur.ru");

        String serialized = mapper.writeValueAsString(EXPECTED_URL);
        System.out.println(serialized);

        TypeReference<HashMap<String, String>> typeRef = new TypeReference<HashMap<String, String>>() {
        };
        Map<String, String> map = mapper.readValue(serialized, typeRef);
        assertEquals(map.get("link"), "/l/" + Base62.to(EXPECTED_URL.getId()));
        assertEquals(map.get("original"), EXPECTED_URL.getOriginal());
        assertEquals(map.get("count"), EXPECTED_URL.getRedirects().toString());
        assertEquals(map.get("rank"), EXPECTED_URL.getRank().toString());
    }
}