package com.volunteer.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    /**
     * Перенаправляет ВСЕ запросы, кроме API и статических файлов, на index.html
     */
    @GetMapping(value = "/**/{path:[^\\.]*}")
    public String forwardToIndex() {
        return "forward:/index.html";
    }

    /**
     * Корневой путь
     */
    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
}