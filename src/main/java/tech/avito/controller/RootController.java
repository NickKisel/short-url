package tech.avito.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {
    @GetMapping
    public String rootPage() {
        return "Welcome to ShorterUrl app!";
    }
}
