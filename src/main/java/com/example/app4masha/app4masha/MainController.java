package com.example.app4masha.app4masha;

import com.example.app4masha.app4masha.service.FileDto;
import com.example.app4masha.app4masha.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@SuppressWarnings("unused")
public class MainController {

    @Autowired
    private FileService fileService;

    @RequestMapping("/")
    @SuppressWarnings("unused")
    public String index(Model model, RedirectAttributes redirectAttributes) throws Exception {

        /*
        FileDto fileDto = new FileDto();
        fileDto.upload = "/uploadRiba";

        redirectAttributes.addFlashAttribute("file1", fileDto);
        model.addAttribute("file", fileDto);

         */
        Object fileDtoObj = model.getAttribute("file");
        if (fileDtoObj == null) {
            fileService.removeDir(FileService.WORK_DIR);
            fileService.createDir(FileService.WORK_DIR);
        } else {
            System.out.println();
        }
        return "index";
    }

}