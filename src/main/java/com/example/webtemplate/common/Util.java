package com.example.webtemplate.common;

import java.net.URI;

public class Util {
    private Util() {
        // Prevent instantiation
    }

    public static URI buildAbsoluteUri(String path) {
        final String BASE_URL = "https://inssider.oomia.click";

        if (path == null || path.isBlank()) {
            path = "/";
        } else if (!path.startsWith("/")) {
            path = java.nio.file.Paths.get("/", path).toString();
        }

        return URI.create(BASE_URL + path);
    }
}