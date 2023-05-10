package com.epam.esm.dbfiller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DatabaseFillerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseFillerApplication.class, args);
    }
}
