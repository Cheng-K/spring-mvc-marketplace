package com.chengk.springmvcmarketplace.domain;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class TokenServiceImpl implements TokenService {

    private KeyPair secretKeyPair;

    public TokenServiceImpl(KeyPair secretKeyPair) {
        this.secretKeyPair = secretKeyPair;
    }

    @Override
    public String generateTokenForUser(String userId) {
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) secretKeyPair.getPublic(),
                (RSAPrivateKey) secretKeyPair.getPrivate());
        String token = JWT.create()
                .withClaim("uid", userId)
                .withIssuer("marketplace-server")
                .withExpiresAt(Instant.now().plusSeconds(15 * 60))
                .sign(algorithm);
        return token;
    }

}
