package com.enofex.naikan.security.token;

import static com.enofex.naikan.test.Tokens.token;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BearerTokenAuthenticationTokenTest {

  @Test
  void shouldConstructCorrectly() {
    String token = token().token();

    BearerTokenAuthenticationToken authenticationToken = new BearerTokenAuthenticationToken(token);

    assertTrue(authenticationToken.isAuthenticated());
    assertEquals(token, authenticationToken.getCredentials());
    assertEquals(token, authenticationToken.getPrincipal());
  }
}