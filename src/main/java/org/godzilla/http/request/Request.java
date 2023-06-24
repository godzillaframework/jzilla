package org.godzilla.http.request;

import org.godzilla.Godzilla;
import org.godzilla.filter.Filter;
import org.godzilla.http.Cookie;
import org.godzilla.utils.Utils;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public class Request {
    private final Godzilla godzilla;

    private final String protocol;
    private final URI uri;
    private final InputStream body;
    private final Headers headers;

    private String context;

    public Request(HttpExchange exchange, Godzilla godzilla) {
        this.godzilla = godzilla;
        this.method = exchange.getRequestMethod();
    }

    public InputStream getBody() {
        return body;
    }
}
