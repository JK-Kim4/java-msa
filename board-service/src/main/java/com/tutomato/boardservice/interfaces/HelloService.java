package com.tutomato.boardservice.interfaces;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloService {

    @GetMapping("/hello")
    public String hello() {
        return "[Board Service] Hello World";
    }

    @GetMapping("/check")
    public String check() {
        return "board service is up";
    }

}
