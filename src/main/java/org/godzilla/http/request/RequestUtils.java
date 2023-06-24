package org.godzilla.http.request;

import com.sun.net.httpserver.Headers;
import org.godzilla.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

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
            } else if(swap) {
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
}
