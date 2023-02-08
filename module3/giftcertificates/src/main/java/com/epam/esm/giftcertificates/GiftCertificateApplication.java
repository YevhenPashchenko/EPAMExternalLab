package com.epam.esm.giftcertificates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GiftCertificateApplication {

  public static void main(String[] args) {
    SpringApplication.run(GiftCertificateApplication.class, args);
  }
}
