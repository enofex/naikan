package com.enofex.naikan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(NaikanProperties.class)
public class NaikanApplication {

  public static void main(String[] args) {
    SpringApplication.run(NaikanApplication.class, args);
  }
}