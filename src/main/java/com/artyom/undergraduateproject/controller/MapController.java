package com.artyom.undergraduateproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MapController {

    private final String API_KEY = System.getenv("API_KEY");

    @GetMapping("/map")
    public String map(Model model) {
        model.addAttribute("apiKey", API_KEY);
        return "google-map";
    }
}
