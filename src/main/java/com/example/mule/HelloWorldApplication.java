package com.example.mule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloWorldApplication {

    public static void main(String[] args) {


        SpringApplication.run(HelloWorldApplication.class, args);

        System.out.println("hello world!");
    }

}
