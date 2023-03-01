package com.raymundo.crypto.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SecretKeyGenerator {

    public String generate() {
        StringBuilder key = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i <= 35; i++)
            key.append((char)('a' + random.nextInt(0, 25)));
        return key.toString();
    }

}