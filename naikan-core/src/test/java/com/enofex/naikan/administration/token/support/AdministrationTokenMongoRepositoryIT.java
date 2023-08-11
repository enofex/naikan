package com.enofex.naikan.administration.token.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.administration.token.AdministrationTokenRepository;
import com.enofex.naikan.administration.token.Token;
import com.enofex.naikan.administration.token.TokenId;
import com.enofex.naikan.test.IntegrationTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

@IntegrationTest
class AdministrationTokenMongoRepositoryIT {

  @Autowired
  private MongoTemplate template;

  private AdministrationTokenRepository repository;

  @BeforeEach
  void setUp() {
    this.repository = new AdministrationTokenMongoRepository(this.template);
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

    this.repository.delete(TokenId.of(savedToken.id()));

    assertEquals(0, this.repository.findAll(Filterable.emptySearch(),
        Pageable.ofSize(10)).getTotalElements());
  }

  @Test
  void shouldExists() {
    Token savedToken = this.repository.save(token());

    assertFalse(this.repository.exists("do_not_exists"));
    assertTrue(this.repository.exists(savedToken.token()));
  }

  @Test
  void shouldUpdateLastUsed() {
    Token savedToken = this.repository.save(token());

    assertEquals(0, this.repository.updateLastUsed("do_not_exists"));
    assertEquals(1, this.repository.updateLastUsed(savedToken.token()));
  }


  private static Token token() {
    return new Token("token",
        "createdBy",
        LocalDateTime.now(),
        LocalDateTime.now(),
        "description");
  }
}