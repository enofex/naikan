package com.enofex.naikan;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.stereotype.Component;

@Component
class EnvironmentLogger implements ApplicationRunner {

  private final Log logger = LogFactory.getLog(getClass());

  private final NaikanProperties naikanProperties;
  private final MongoProperties mongoProperties;

  EnvironmentLogger(NaikanProperties naikanProperties, MongoProperties mongoProperties) {
    this.naikanProperties = naikanProperties;
    this.mongoProperties = mongoProperties;
  }

  @Override
  public void run(ApplicationArguments args) {
    mongodb();
    ldap();
  }

  private void mongodb() {
    this.logger.info("Mongodb uri: %s".formatted(this.mongoProperties.getUri()));
    this.logger.info("Mongodb database: %s".formatted(this.mongoProperties.getDatabase()));

    this.logger.info("Mongodb transaction enabled: %s".formatted(
        this.naikanProperties.mongodb().transaction().enable()));
  }

  private void ldap() {
    this.logger.info(
        "Ldap enabled: %s".formatted(this.naikanProperties.security().ldap().enabled()));
  }
}
