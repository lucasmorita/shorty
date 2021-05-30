package org.eldron.shorty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShortyApplication {

    public static void main(String[] args) {
        System.out.println("Iniciando");
        SpringApplication.run(ShortyApplication.class, args);
    }

}
