package com.enofex.naikan.test;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;

@TestConfiguration(proxyBeanMethods = false)
class ContainersConfiguration {

  @Bean
  @ServiceConnection
  MongoDBContainer mongoDBContainer() {
    return new MongoDBContainer("mongo:5");
  }
}
