package com.carloprogram.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@ComponentScan(basePackages = "com.carloprogram")
@EntityScan(basePackages = "com.carloprogram.model")
@EnableJpaRepositories(basePackages = "com.carloprogram.repository")
public class MainApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(MainApplication.class, args);
    }

}