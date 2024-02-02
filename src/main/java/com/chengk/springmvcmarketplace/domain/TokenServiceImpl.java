package com.chengk.springmvcmarketplace.domain;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class TokenServiceImpl implements TokenService {
    ;
    private Algorithm algorithm;

    public TokenServiceImpl(KeyPair secretKeyPair) {
        this.algorithm = Algorithm.RSA256((RSAPublicKey) secretKeyPair.getPublic(),
                (RSAPrivateKey) secretKeyPair.getPrivate());
    }

    @Override
    public String generateTokenForUser(String userId) {
        String token = JWT.create()
                .withClaim("uid", userId)
                .withIssuer("marketplace-server")
                .withExpiresAt(Instant.now().plusSeconds(15 * 60))
                .sign(algorithm);
        return token;
    }

    @Override
    public String verifyAndGetClaim(String token, String claim) throws JWTVerificationException {
        DecodedJWT decoded = JWT.require(algorithm)
                .withIssuer("marketplace-server")
                .withClaimPresence(claim)
                .build()
                .verify(token);
        return decoded.getClaim(claim).asString();
    }

}
