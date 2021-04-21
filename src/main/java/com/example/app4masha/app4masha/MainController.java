package com.example.app4masha.app4masha;

import com.example.app4masha.app4masha.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@SuppressWarnings("unused")
public class MainController {

    @Autowired
    private FileService fileService;

    @RequestMapping("/")
    @SuppressWarnings("unused")
    public String helloWorld(Model model) {
        return "index";
    }

    @RequestMapping("/test")
    public String test(Model model) {
        List<String> strings = fileService.getValueCaptionsForReplace(null);
        model.addAttribute("inputValues", strings);
        return "test";
    }

}