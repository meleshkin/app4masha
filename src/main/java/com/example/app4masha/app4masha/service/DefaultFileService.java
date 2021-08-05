package com.example.app4masha.app4masha.service;

import com.example.app4masha.app4masha.service.exception.StorageException;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultFileService implements FileService {

    private String location = ".";
    private Path rootLocation = Path.of(location);


    @PostConstruct
    public void init() {

    }

    @Override
    public String getFilePath(String fileName) {
        Path destinationFile = this.rootLocation.resolve(
                Paths.get(WORK_DIR, fileName));
                if (destinationFile != null) {
                    Path path =  destinationFile.normalize().toAbsolutePath();
                    if (Files.exists(path)) {
                        return path.toString();
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }
    }

    @Override
    public Path save(MultipartFile file, String targetName) throws StorageException {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(
                    Paths.get(WORK_DIR, targetName))
                    .normalize().toAbsolutePath();
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return destinationFile;
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public void createDir(String dirName) throws StorageException {
        try {
            Files.createDirectories(Path.of(location, dirName));
        } catch (IOException e) {
            throw new StorageException("Failed to create dir.", e);
        }
    }

    @Override
    public void removeDir(String dirName) throws StorageException{
        try {
            Path dirPath = this.rootLocation.resolve(
                    Paths.get(dirName)
                    .normalize().toAbsolutePath());
            FileUtils.deleteDirectory(dirPath.toFile());
        } catch (IOException e) {
            throw new StorageException("Failed to remove dir.", e);
        }
    }

    @Override
    public String getWorkDirName() {
        return this.rootLocation.resolve(Paths.get(WORK_DIR).normalize().toAbsolutePath()).toString();
    }


}
