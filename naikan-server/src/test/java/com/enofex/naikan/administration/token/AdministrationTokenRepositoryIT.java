package com.enofex.naikan.administration.token;

import static com.enofex.naikan.test.Tokens.token;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.enofex.naikan.security.token.Token;
import com.enofex.naikan.test.IntegrationTest;
import com.enofex.naikan.web.Filterable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

@IntegrationTest
class AdministrationTokenRepositoryIT {

  @Autowired
  private MongoTemplate template;

  private AdministrationTokenRepository repository;

  @BeforeEach
  void setUp() {
    this.repository = new AdministrationTokenRepository(this.template);
  }

  @Test
  void shouldFindAll() {
    this.repository.save(token());

    assertEquals(1, this.repository.findAll(Filterable.emptySearch(),
        Pageable.ofSize(10)).getTotalElements());

    assertEquals(0, this.repository.findAll(Filterable.of("do_not_exists"),
        Pageable.ofSize(10)).getTotalElements());

    assertEquals(1, this.repository.findAll(Filterable.of(token().token()),
        Pageable.ofSize(10)).getTotalElements());
  }

  @Test
  void shouldDelete() {
    Token savedToken = this.repository.save(token());

    assertEquals(1, this.repository.findAll(Filterable.emptySearch(),
        Pageable.ofSize(10)).getTotalElements());

    this.repository.deleteById(TokenId.of(savedToken.id()));

    assertEquals(0, this.repository.findAll(Filterable.emptySearch(),
        Pageable.ofSize(10)).getTotalElements());
  }
}