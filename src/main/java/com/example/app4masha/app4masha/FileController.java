package com.example.app4masha.app4masha;

import com.example.app4masha.app4masha.service.FileDto;
import com.example.app4masha.app4masha.service.FileService;
import com.example.app4masha.app4masha.service.exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Controller()
@SuppressWarnings("unused")
public class FileController {

    @Autowired
    private FileService fileService;

    private Set<Path> uploadedFiles = new HashSet<>();

    @PostMapping("/upload")
    @SuppressWarnings("unused")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("fileType") String fileType,
           RedirectAttributes redirectAttributes, Model model) throws StorageException {

        if ("riba".equals(fileType)) {

            Path dest = fileService.save(file, FileService.RIBA_NAME);
            uploadedFiles.add(dest);

            FileDto fileDto = new FileDto();
            fileDto.riba = dest.toString();
            fileDto.info = fileService.getFilePath(FileService.INFO_NAME);
            fileDto.nextUpload = "info";

            redirectAttributes.addFlashAttribute("file", fileDto);
            model.addAttribute("riba", fileDto.riba);
        } else {
            Path dest = fileService.save(file, FileService.INFO_NAME);
            uploadedFiles.add(dest);
            FileDto fileDto = new FileDto();
            fileDto.info = dest.toString();
            fileDto.riba = fileService.getFilePath(FileService.RIBA_NAME);
            fileDto.nextUpload = "ready";

            redirectAttributes.addFlashAttribute("file", fileDto);
        }

        return "redirect:/";
    }

    @GetMapping(path = "/file")
    public ResponseEntity<ByteArrayResource> download(@RequestParam("file") String file) throws IOException {

       // InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        File file2Upload = new File(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        Path path = Paths.get(file2Upload.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file2Upload.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
