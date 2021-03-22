package com.example.app4masha.app4masha;

import com.example.app4masha.app4masha.service.FileService;
import com.example.app4masha.app4masha.service.exception.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller()
@SuppressWarnings("unused")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    @SuppressWarnings("unused")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws StorageException {

        fileService.save(file);
        redirectAttributes.addFlashAttribute("message",
                "Успешно загружен " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }
}
