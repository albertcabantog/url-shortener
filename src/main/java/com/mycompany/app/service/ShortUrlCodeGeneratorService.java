package com.mycompany.app.service;

import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

@Service
public class ShortUrlCodeGeneratorService {

    public String generateUniqueCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public boolean checkUrlFormat(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
