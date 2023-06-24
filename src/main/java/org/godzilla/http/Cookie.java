package org.godzilla.http;

import java.time.Instant;

public class Cookie {

    private String name, value;
    private String expire;
    private String path = "/";
    private String domain;
    private String sameSite;
    private boolean secure = false;
    private boolean httpOnly = false;
    private long maxAge = -1;

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

    public Cookie setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Cookie setValue(String value) {
        this.value = value;
        return this;
    }

    public String getExpire() {
        return expire;
    }

    public Cookie setExpire(Instant instant) {
        this.expire = instant.toString();
        return this;
    }

    public String getPath() {
        return path;
    }

    public Cookie setPath(String path) {
        this.path = path;
        return this;
    }

    public boolean isSecure() {
        return secure;
    }

    public Cookie setSecure(boolean secure) {
        this.secure = secure;
        return this;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public Cookie setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    public String getSameSite() {
        return sameSite;
    }

    public Cookie setSameSite(SameSite sameSite) {
        if (sameSite == null) return this;
        this.sameSite = sameSite.name();
        return this;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public Cookie setMaxAge(long maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cookie) {
            Cookie other = (Cookie) obj;

            if (!other.getValue().equals(this.getValue())) return false;
            if (!other.getName().equals(this.getName())) return false;
            if (!other.getDomain().equals(this.getDomain())) return false;
            if (!other.getExpire().equals(this.getExpire())) return false;
            if (other.getMaxAge() != this.getMaxAge()) return false;
            if (!other.getSameSite().equals(this.getSameSite())) return false;

            return other.getPath().equals(this.getPath());
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        if (name == null || value == null)
            return null;

        StringBuilder b = new StringBuilder();
        b.append(name).append("=").append(value);

        if (path != null) b.append("; Path=").append(path);
        if (expire != null) b.append("; Expire=").append(expire);
        if (maxAge != -1) b.append("; Max-Age=").append(maxAge);

        if (domain != null) b.append("; Domain=").append(domain);
        if (sameSite != null) b.append("; SameSite=").append(sameSite);

        if (secure) b.append("; Secure");
        if (httpOnly) b.append("; HttpOnly");

        return b.toString();
    }
}