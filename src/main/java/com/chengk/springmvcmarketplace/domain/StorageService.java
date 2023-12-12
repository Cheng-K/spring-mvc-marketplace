package com.chengk.springmvcmarketplace.domain;

public interface StorageService {
    void saveFile(byte[] bytes, String fileName, String directoryPath);
}
