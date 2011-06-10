package org.adaptiveplatform.surveys.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HttpResponseUtils {

    public static HttpEntity<byte[]> createHttpResponse(byte[] body, String contentType) {
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", contentType);
        header.set("Content-Length", String.valueOf(body.length));
        return new ResponseEntity<byte[]>(body, header, HttpStatus.OK);
    }

    public static HttpEntity<String> createHttpResponse(String body, String contentType) {
        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", contentType);
        header.set("Content-Length", String.valueOf(body.length()));
        return new ResponseEntity<String>(body, header, HttpStatus.OK);
    }
}
