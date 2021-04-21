package com.example.app4masha.app4masha.service;

import com.example.app4masha.app4masha.service.exception.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    void save(MultipartFile file) throws StorageException;

    List<String> getValueCaptionsForReplace(String fileContent);
}
