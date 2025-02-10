package com.example.demoo.util;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Генерация ключа
    private static final long EXPIRATION_TIME = 86400000; // 1 день (в миллисекундах)

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // Устанавливаем имя пользователя
                .setIssuedAt(new Date()) // Время создания
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Истекает через 1 день
                .signWith(SECRET_KEY) // Подписываем ключом
                .compact();
    }
}

