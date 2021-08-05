package com.example.app4masha.app4masha.service;

import com.example.app4masha.app4masha.service.exception.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface FileService {
    public static final String WORK_DIR = "work";
    public static final String RIBA_NAME = "riba.docx";
    public static final String INFO_NAME = "info.xlsx";

    String getFilePath(String fileName);

    Path save(MultipartFile file, String targetName) throws StorageException;

    void createDir(String dirName) throws StorageException;

    void removeDir(String dirName) throws StorageException;

    String getWorkDirName();
}
