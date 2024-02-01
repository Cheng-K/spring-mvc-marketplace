package com.chengk.springmvcmarketplace.domain;

public interface TokenService {

    String generateTokenForUser(String userId);

}
