package org.godzilla.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.security.SecureRandom;

public final class Utils {
    private Utils() {}

    public static String streamToString(InputStream is) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line;
            
            return sb.toString();
        } catch (IOException ignored) {
        }

        return null;
    }

    public static String getYourIP() throws UnknownHostException {
        return Inet4Address.getLocalHost().getHostAddress();
    }

    public static String getExtension(Path file) {
        String path = file.getFileName().toString();
        int index = path.lastIndexOf('.') + 1;

        if (index == 0) {
            return null;
        }

        return path.substring(index);
    }
}
