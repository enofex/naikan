package com.enofex.naikan;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

@ConfigurationProperties(prefix = "naikan")
record NaikanProperties(Mongodb mongodb, Security security) {

  record Mongodb(Transaction transaction) {

    public record Transaction(boolean enable) {

    }
  }

  record Security(Ldap ldap, CorsConfiguration cors) {

    public record Ldap(boolean enabled, String userDnPatterns, String userSearchBase,
                       String userSearchFilter, String activeDirectoryDomain) {

    }
  }
}

