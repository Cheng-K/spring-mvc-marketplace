package com.chengk.springmvcmarketplace.domain;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public class StorageServiceImpl implements StorageService {

    @Override
    public void saveFile(byte[] bytes, String fileName, String directoryPath) {
        try {
            FileOutputStream stream = new FileOutputStream(directoryPath + "/" + fileName);
            stream.write(bytes);
            stream.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }

}
