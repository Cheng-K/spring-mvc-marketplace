package com.chengk.springmvcmarketplace.domain;

public interface EmailService {
    void send(String from, String to, String subject, String content);
}
