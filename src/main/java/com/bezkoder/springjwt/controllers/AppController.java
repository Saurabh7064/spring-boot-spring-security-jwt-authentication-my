package com.bezkoder.springjwt.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@Controller
@RequestMapping("/api/test")
public class AppController {

        @RequestMapping({"/"})
    public String loadUI() {
        return "forward:/index.html";
    }
    @RequestMapping({"/react"})
    public String loadUI2() {
        return "forward:/index.html";
    }

}
