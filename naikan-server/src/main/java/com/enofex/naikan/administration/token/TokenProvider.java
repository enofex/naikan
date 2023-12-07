package com.enofex.naikan.administration.token;

import java.security.SecureRandom;
import java.util.Base64;
import org.springframework.stereotype.Component;

@Component
final class TokenProvider {

  String token() {
    byte[] token = new byte[32];
    new SecureRandom().nextBytes(token);

    return Base64.getUrlEncoder().withoutPadding().encodeToString(token);
  }
}