package com.enofex.naikan.administration.token;

import com.enofex.naikan.Filterable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdministrationTokenRepository {

  Page<Token> findAll(Filterable filterable, Pageable pageable);

  Token save(Token token);

  long updateLastUsed(String token);

  long deleteById(TokenId id);

  boolean exists(String token);

}
