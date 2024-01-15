package com.chengk.springmvcmarketplace.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

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

    @Override
    public void deleteFile(String filePath) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.delete()) {
            throw new Exception("Unable to delete the file");
        }
    }

    @Override
    public String replaceFileNameWithUUID(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + fileExtension;
        return newFileName;
    }

    @Override
    public boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.isFile() && file.exists();
    }

}
