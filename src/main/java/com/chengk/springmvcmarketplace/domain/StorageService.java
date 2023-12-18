package com.chengk.springmvcmarketplace.domain;

public interface StorageService {
    void saveFile(byte[] bytes, String fileName, String directoryPath);

    void deleteFile(String filePath) throws Exception;

    String replaceFileNameWithUUID(String fileName);
}
