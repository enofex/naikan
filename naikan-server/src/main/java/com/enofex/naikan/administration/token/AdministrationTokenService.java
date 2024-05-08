package com.enofex.naikan.administration.token;

import com.enofex.naikan.security.token.Token;
import com.enofex.naikan.web.Filterable;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class AdministrationTokenService {

  private final AdministrationTokenRepository administrationTokenRepository;
  private final TokenProvider tokenProvider;

  AdministrationTokenService(AdministrationTokenRepository administrationTokenRepository,
      TokenProvider tokenProvider) {
    this.administrationTokenRepository = administrationTokenRepository;
    this.tokenProvider = tokenProvider;
  }

  public Page<Token> findAll(Filterable filterable, Pageable pageable) {
    return this.administrationTokenRepository.findAll(filterable, pageable);
  }

  @Transactional
  public Token save(String description) {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();

    Token token = new Token(
        this.tokenProvider.token(),
        name,
        LocalDateTime.now(),
        null,
        description);

    return this.administrationTokenRepository.save(token);
  }

  @Transactional
  public long deleteById(TokenId id) {
    return this.administrationTokenRepository.deleteById(id);
  }
}
