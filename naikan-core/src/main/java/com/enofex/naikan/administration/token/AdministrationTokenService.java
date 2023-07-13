package com.enofex.naikan.administration.token;

import com.enofex.naikan.Filterable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdministrationTokenService {

  Page<Token> findAll(Filterable filterable, Pageable pageable);

  Token save(String description);

  long updateLastUsed(String token);

  long delete(TokenId id);

  boolean exists(String token);
}
