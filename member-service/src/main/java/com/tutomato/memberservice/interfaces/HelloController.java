package com.tutomato.memberservice.interfaces;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "[Member Service] Hello World";
    }

    @GetMapping("/check")
    public String check() {
        return "board service is up";
    }
}
