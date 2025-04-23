package org.example.auth.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

@Slf4j
@Component
public class CookieUtils {

    public Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    log.debug(cookie.getName().toString());
                    return Optional.of(cookie);
                }
            }
        }

        return Optional.empty();
    }

    public void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        // cookie.setSecure(true);
        cookie.setMaxAge(maxAge);

        cookie.setAttribute("SameSite", "None");

        String headerValue = String.format("%s=%s; Path=/; HttpOnly; Secure; SameSite=None; Max-Age=%d",
                name, value, maxAge);
        response.setHeader("Set-Cookie", headerValue);
        response.setHeader("Access-Control-Allow-Origin", "https://b491-61-85-165-157.ngrok-free.app");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        response.addCookie(cookie);
    }

    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    public <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue())
        ));
    }
}
