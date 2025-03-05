package com.carloprogram.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;


@SpringBootApplication
@ComponentScan(basePackages = "com.carloprogram")
@EntityScan(basePackages = "com.carloprogram.model")
@EnableJpaRepositories(basePackages = "com.carloprogram.repository")
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
@EnableCaching
public class MainApplication {

    public static void main(String[] args)
    {
        SpringApplication.run(MainApplication.class, args);
    }

}