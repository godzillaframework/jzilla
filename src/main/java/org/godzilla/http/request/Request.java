package org.godzilla.http.request;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpsExchange;
import org.godzilla.Godzilla;
import org.godzilla.filter.Filter;
import org.godzilla.http.Cookie;
import org.godzilla.utils.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
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
    private final boolean secure;
    private final String contentType;                   
    private final long contentLength;                   
    private final String method;                        
    private final List<Auth> auth;             
    private final InetSocketAddress inet;               

    private final HashMap<String, Object> middleware;   
    private final HashMap<String, Cookie> cookies;      
    private final HashMap<String, String> queries;      
    private final HashMap<String, String> formQueries; 

    private HashMap<String, String> params;            
    private String context;                            

    {
        this.middleware = new HashMap<>();
        this.params = new HashMap<>();
    }

    public Request(HttpExchange exchange, Godzilla godzilla) {
        this.godzilla = godzilla;
        this.method = exchange.getRequestMethod();
        this.uri = exchange.getRequestURI();
        this.headers = exchange.getRequestHeaders();
        this.body = exchange.getRequestBody();
        this.inet = exchange.getRemoteAddress();

        this.protocol = exchange.getProtocol();
        this.secure = exchange instanceof HttpsExchange; 

        String contentLength = headers.get("Content-Length") != null ? headers.get("Content-Length").get(0) : null;
        this.contentLength = contentLength != null ? Long.parseLong(contentLength) : -1;

        this.contentType = headers.get("Content-Type") == null ? "" : headers.get("Content-Type").get(0);

        this.auth = Auth.get(this);

        this.formQueries = contentType.startsWith("application/x-www-form-urlencoded")
                ? RequestUtils.parseRawQuery(Utils.streamToString(body))
                : new HashMap<>();

        this.queries = RequestUtils.parseRawQuery(exchange.getRequestURI().getRawQuery());
        this.cookies = RequestUtils.parseCookies(headers);
    }

    public InputStream getBody() {
        return body;
    }

    public void pipe(OutputStream os, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int n;
        while ((n = body.read(buffer)) != -1)
            os.write(buffer, 0, n);
        os.close();
    }

    public void pipe(Path f, int bufferSize) throws IOException {
        if (Files.exists(f))
            return;

        Files.createFile(f);
        pipe(Files.newOutputStream(f), bufferSize);
    }

    public Cookie getCookie(String name) {
        return cookies.get(name);
    }

    public HashMap<String, Cookie> getCookies() {
        return cookies;
    }

    public void addMiddlewareContent(Filter middleware, Object middlewareData) {
        this.middleware.put(middleware.getName(), middlewareData);
    }

    public Object getMiddlewareContent(String name) {
        return middleware.get(name);
    }

    public String getUserAgent() {
        return headers.get("User-agent").get(0);
    }

    public String getHost() {
        return headers.get("Host").get(0);
    }

    public InetAddress getAddress() {
        return inet.getAddress();
    }

    public String getIp() {
        return inet.getAddress().getHostAddress();
    }

    public String getContentType() {
        return contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public String getPath() {
        return this.uri.getPath();
    }

    public URI getURI() {
        return this.uri;
    }

    public String getMethod() {
        return this.method;
    }

    public boolean isFresh() {

        if (headers.containsKey("cache-control") && headers.get("cache-control").get(0) != null && headers.get("cache-control").get(0).equals("no-cache"))
            return true;

        if (headers.containsKey("if-none-match") && headers.get("if-none-match").get(0) != null && headers.get("if-none-match").get(0).equals("*"))
            return true;

        if (headers.containsKey("if-modified-since") && headers.containsKey("last-modified") && headers.containsKey("modified")) {
            List<String> lmlist = headers.get("last-modified");
            List<String> mlist = headers.get("modified");

            if (lmlist.isEmpty() || mlist.isEmpty())
                return false;

            String lm = lmlist.get(0);
            String m = mlist.get(0);

            if (lm != null && m != null) {
                try {

                    Instant lmi = Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(lm));
                    Instant mi = Instant.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(m));

                    if (lmi.isBefore(mi) || lmi.equals(mi)) {
                        return true;
                    }

                } catch (Exception ignored) {
                }
            }
        }

        return false;
    }

    public boolean isStale() {
        return !isFresh();
    }

    public boolean isSecure() {
        return secure;
    }

    public boolean isXHR() {
        return headers.containsKey("X-Requested-With") && !headers.get("X-Requested-With").isEmpty() && headers.get("X-Requested-With").get(0).equals("XMLHttpRequest");
    }

    public String getProtocol() {
        return protocol;
    }

    public List<Auth> getAuthorization() {
        return Collections.unmodifiableList(auth);
    }

    public boolean hasAuthorization() {
        return !auth.isEmpty();
    }

    public String getFormQuery(String name) {
        return formQueries.get(name);
    }

    public String getParam(String param) {
        return params.get(param);
    }

    public String getQuery(String name) {
        return queries.get(name);
    }

    public HashMap<String, String> getFormQuerys() {
        return formQueries;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public HashMap<String, String> getQuerys() {
        return queries;
    }

    public List<String> getHeader(String header) {
        return Optional.ofNullable(headers.get(header)).orElse(Collections.emptyList());
    }

    public Godzilla getApp() {
        return godzilla;
    }

}