package com.deepdhamala.filmpatro.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile multipartFile);
}
