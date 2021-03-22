package com.example.app4masha.app4masha;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@SuppressWarnings("unused")
public class MainController {

    @RequestMapping("/")
    @SuppressWarnings("unused")
    public String helloWorld(Model model) {
        return "index";
    }

}