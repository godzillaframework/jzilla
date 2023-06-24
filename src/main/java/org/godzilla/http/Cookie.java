package org.godzilla.http;

import java.time.Instant;

public class Cookie {
    private String name, value;
    private String expire;
    private String path = "/";
    private String domain;
    private String sameSite;
    private boolean secure = false;

    public Cookie(String name, String value) {
        name = name.trim();

        if (name.isEmpty() || name.charAt(0) == '$') {
            throw new IllegalArgumentException("Illegal cookie name");
        }

        this.name = name;
        this.value = value;
    }

    public Cookie() {
    }

    public String getName() {
        return name;
    }

    public String setName(String name) {
        this.name = name;
        // return this;
        return name;
    }

    @Override
    public String toString() {
        if (name == null || value == null)
            return null;
        
        StringBuilder b = new StringBuilder();
        b.append(name).append("=").append(value);

        if(path != null) b.append("; Path=").append(path);

        return b.toString();
    }
}
