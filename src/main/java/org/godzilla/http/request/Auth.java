package org.godzilla.http.request;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Auth {

    public static final String HEADER_NAME = "Authorization";

    private final String type;
    private final String data;

    public Auth(String authHeader) {
        String[] parts = Stream.of(authHeader.split(" "))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        this.type = parts[0];
        this.data = parts[1];
    }

    public static List<Auth> get(Request req) {
        List<String> headerVals = req.getHeader(HEADER_NAME);

        if (!headerVals.isEmpty()) {
            String authHeader = headerVals.get(0);
            return Collections.unmodifiableList(Stream.of(authHeader.split(","))
                    .map(Auth::new).collect(Collectors.toList()));
        }

        return Collections.emptyList();
    }

    @SafeVarargs
    public static boolean validate(Request req, Predicate<Auth>... validators) {
        for (Auth auth : get(req)) {
            for (Predicate<Auth> validator : validators) {
                if (validator.test(auth)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Predicate<Auth> validator(String type, String data) {
        return (auth -> auth.getType().equals(type) && auth.getData().equals(data));
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