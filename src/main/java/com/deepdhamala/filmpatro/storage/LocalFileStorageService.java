package com.deepdhamala.filmpatro.storage;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService{

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(@NotNull MultipartFile file) {
        try {
            String originalName = file.getOriginalFilename();
            String fileName = UUID.randomUUID() + "_" + originalName;
            Path targetLocation = Paths.get(uploadDir);
            if (!Files.exists(targetLocation)) {
                Files.createDirectories(targetLocation);
            }
            Path filePath = targetLocation.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + fileName;  // for web serving
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file. Error: " + ex.getMessage());
        }
    }
}
