package com.enofex.naikan.security.token;

import java.time.LocalDateTime;

public record Token(String id, String token, String createdBy, LocalDateTime created,
                    LocalDateTime lastUsed, String description) {

  public Token(String token, String createdBy, LocalDateTime created, LocalDateTime lastUsed,
      String description) {
    this(null, token, createdBy, created, lastUsed, description);
  }
}
