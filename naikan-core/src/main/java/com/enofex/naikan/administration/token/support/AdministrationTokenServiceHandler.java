package com.enofex.naikan.administration.token.support;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.administration.token.AdministrationTokenRepository;
import com.enofex.naikan.administration.token.AdministrationTokenService;
import com.enofex.naikan.administration.token.Token;
import com.enofex.naikan.administration.token.TokenId;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class AdministrationTokenServiceHandler implements AdministrationTokenService {

  private final AdministrationTokenRepository administrationTokenRepository;
  private final TokenProvider tokenProvider;

  AdministrationTokenServiceHandler(AdministrationTokenRepository administrationTokenRepository,
      TokenProvider tokenProvider) {
    this.administrationTokenRepository = administrationTokenRepository;
    this.tokenProvider = tokenProvider;
  }

  @Override
  public Page<Token> findAll(Filterable filterable, Pageable pageable) {
    return administrationTokenRepository.findAll(filterable, pageable);
  }

  @Override
  public Token save(String description) {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();

    Token token = new Token(
        tokenProvider.token(),
        name,
        LocalDateTime.now(),
        null,
        description);

    return administrationTokenRepository.save(token);
  }

  @Override
  public long updateLastUsed(String token) {
    return administrationTokenRepository.updateLastUsed(token);
  }

  @Override
  public long delete(TokenId id) {
    return administrationTokenRepository.delete(id);
  }

  @Override
  public boolean exists(String token) {
    return administrationTokenRepository.exists(token);
  }
}
