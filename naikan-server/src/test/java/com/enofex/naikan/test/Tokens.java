package com.enofex.naikan.test;

import com.enofex.naikan.security.token.Token;
import java.time.LocalDateTime;

public final class Tokens {

  private Tokens() {
  }

  public static Token token() {
    return new Token("token",
        "createdBy",
        LocalDateTime.now(),
        LocalDateTime.now(),
        "description");
  }
}
