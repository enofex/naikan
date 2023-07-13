package com.enofex.naikan;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "naikan", name = "security.cors.enable", havingValue = "true", matchIfMissing = true)
class CorsFilterConfiguration {

  private final Log logger = LogFactory.getLog(getClass());

  @Bean
  @ConfigurationProperties(prefix = "naikan.security.cors")
  CorsConfiguration corsConfiguration() {
    return new CorsConfiguration();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource(CorsConfiguration configuration) {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    if (configuration != null) {
      if (this.logger.isDebugEnabled()) {
        this.logger.debug("Registering CORS filter");
      }
      source.registerCorsConfiguration("/**", configuration.applyPermitDefaultValues());
    }

    return source;
  }

}