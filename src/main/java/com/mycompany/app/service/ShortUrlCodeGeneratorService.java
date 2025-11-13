package com.mycompany.app.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ShortUrlCodeGeneratorService {

    public String generateUniqueCode(String url) {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
