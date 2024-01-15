package com.chengk.springmvcmarketplace.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.chengk.springmvcmarketplace.domain.StorageService;

@Controller
public class ImagesController {

    private StorageService storageService;

    public ImagesController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/images/{*filepath}")
    public ResponseEntity<byte[]> getImage(@PathVariable("filepath") String filepath) {
        String filePath = "src/main/resources/images" + filepath;
        String contentType = "";
        byte[] fileBytes = null;

        // check if file exists
        if (!storageService.fileExists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        // figure out the content type
        try {
            fileBytes = Files.readAllBytes(Paths.get(filePath));
            contentType = Files.probeContentType(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().header("Content-Type", contentType)
                .body(fileBytes);
    }

}
