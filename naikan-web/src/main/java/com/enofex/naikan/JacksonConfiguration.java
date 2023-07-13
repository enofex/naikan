package com.enofex.naikan;

import com.enofex.naikan.model.module.NaikanModule;
import com.fasterxml.jackson.databind.Module;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
class JacksonConfiguration {

  private final Log logger = LogFactory.getLog(getClass());

  @Bean
  Module naikanModule() {
    if (this.logger.isDebugEnabled()) {
      this.logger.debug("Registering Naikan Jackson Module");
    }

    return new NaikanModule();
  }
}