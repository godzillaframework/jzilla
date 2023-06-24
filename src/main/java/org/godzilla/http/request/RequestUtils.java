package org.godzilla.http.request;

import org.godzilla.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import com.sun.net.httpserver.Headers;

final class RequestUtils {

    private RequestUtils() {}

    static HashMap<String, Cookie> parseCookies(Headers headers) {
        HashMap<String, Cookie> cookieList = new HashMap<>();
        List<String> headerCookies = headers.get("Cookie");

        if (headerCookies == null || headerCookies.isEmpty()) {
            return cookieList;
        }

        char[] chars = headerCookies.get(0).toCharArray();
        StringBuilder key = new StringBuilder();
        StringBuilder val = new StringBuilder();
        boolean swap = false;

        for (char c : chars) {
            if (c == '=') {
                swap = true;
            } else if (c == ';') {
                String rkey = key.toString().trim();
                cookieList.put(rkey, new Cookie(rkey, val.toString()));

                key.setLength(0);
                val.setLength(0);
                swap = false;
            } else if (swap) {
                val.append(c);
            } else {
                key.append(c);
            }
        }

        if (key.length() > 0 && val.length() > 0) {
            String rkey = key.toString().trim();
            cookieList.put(rkey, new Cookie(rkey, val.toString()));
        }

        return cookieList;
    }

    static HashMap<String, String> parseRawQuery(String rawQuery) {
        HashMap<String, String> querys = new HashMap<>();

        if (rawQuery == null) {
            return querys;
        }

        StringBuilder key = new StringBuilder();
        StringBuilder val = new StringBuilder();
        char[] chars = rawQuery.toCharArray();
        boolean keyac = false;
        char c = '=';

        for (char cc : chars) {
            c = cc;

            if (c == '=') {
                keyac = true;
            } else if (c == '&') {

                try {
                    querys.put(URLDecoder.decode(key.toString(), "UTF-8"), URLDecoder.decode(val.toString(), "UTF8"));
                } catch (UnsupportedEncodingException ignored) {
                }

                key.setLength(0);
                val.setLength(0);
                keyac = false;
            } else if (keyac) {
                val.append(c);
            } else {
                key.append(c);
            }
        }

        if (c != '=' && c != '&') {
            querys.put(key.toString(), val.toString());
        }

        return querys;
    }

}