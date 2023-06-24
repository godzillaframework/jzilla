package org.godzilla.http.request;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class Auth {
    public static final String HEADER_NAME = "Authorization";

    private final String type;
    private final String data;

    public Authorization(String authHeader) {
        String[] parts = Stream.of(authHeader.split(" ")).filter(s -> !s.isEmpty()).toArray(String[]::new);
        this.type = parts[0];
        this.data = parts[1];
    }


    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public String getDataBase64Decoded() {
        return new String(Base64.getDecoder().decode(data));
    }
}
