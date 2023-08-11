package com.enofex.naikan.security.token;

import java.io.Serial;
import org.springframework.security.authentication.AbstractAuthenticationToken;

final class BearerTokenAuthenticationToken extends AbstractAuthenticationToken {

  @Serial
  private static final long serialVersionUID = -6900859933732307227L;

  private final String token;

  BearerTokenAuthenticationToken(String token) {
    super(null);
    this.token = token;
    setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return token;
  }

  @Override
  public Object getPrincipal() {
    return token;
  }
}
