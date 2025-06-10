package com.vamospassar.respostabot.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.vamospassar.respostabot.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {
    private final String SECRET = "adfaff";
    private final String INSSUER = "login-api";

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);

            String token = JWT.create()
                    .withIssuer(INSSUER)
                    .withSubject(user.getEmail())
                    .withExpiresAt(getExpireDate())
                    .sign(algorithm);
            return token;

        } catch (JWTCreationException error) {
            throw new RuntimeException("Error while generation token ", error);
        }

    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);

        try {

            return JWT.require(algorithm)
                    .withIssuer(INSSUER)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException error) {
            throw new RuntimeException("Error while verification token ", error);
        }


    }

    private Instant getExpireDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}


