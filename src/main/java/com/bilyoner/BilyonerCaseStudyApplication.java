package com.bilyoner;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class BilyonerCaseStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BilyonerCaseStudyApplication.class, args);
    }

}
