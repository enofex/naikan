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
    logger.info("Mongodb uri: %s".formatted(mongoProperties.getUri()));
    logger.info("Mongodb database: %s".formatted(mongoProperties.getDatabase()));

    logger.info("Mongodb transaction enabled: %s".formatted(
        naikanProperties.mongodb().transaction().enable()));
  }

  private void ldap() {
    logger.info(
        "Ldap enabled: %s".formatted(naikanProperties.security().ldap().enabled()));
  }
}
