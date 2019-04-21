package org.xujin.moss.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {
    public static void main(String[] args) {
        SpringApplication.run( Application.class, args );
    }

    @GetMapping("/test")
    public Object test(){
        return "";
    }
    @GetMapping("/foo")
    public Object foo(){
        return "";
    }
}