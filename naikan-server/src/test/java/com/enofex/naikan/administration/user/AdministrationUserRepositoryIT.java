package com.enofex.naikan.administration.user;

import static com.enofex.naikan.test.Users.user;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.enofex.naikan.security.User;
import com.enofex.naikan.test.IntegrationTest;
import com.enofex.naikan.web.Filterable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

@IntegrationTest
class AdministrationUserRepositoryIT {

  @Autowired
  private MongoTemplate template;

  private AdministrationUserRepository repository;

  @BeforeEach
  void setUp() {
    this.repository = new AdministrationUserRepository(this.template);
  }

  @Test
  void shouldFindAll() {
    this.repository.save(user());

    assertEquals(1, this.repository.findAll(Filterable.emptySearch(),
        Pageable.ofSize(10)).getTotalElements());

    assertEquals(0, this.repository.findAll(Filterable.of("do_not_exists"),
        Pageable.ofSize(10)).getTotalElements());

    assertEquals(1, this.repository.findAll(Filterable.of(user().name()),
        Pageable.ofSize(10)).getTotalElements());
  }

  @Test
  void shouldFindById() {
    User savedUser = this.repository.save(user());

    assertEquals(savedUser.id(), this.repository.findById(UserId.of(savedUser.id())).id());
  }

  @Test
  void shouldDeleteById() {
    User savedUser = this.repository.save(user());

    assertEquals(1, this.repository.findAll(Filterable.emptySearch(),
        Pageable.ofSize(10)).getTotalElements());

    this.repository.deleteById(UserId.of(savedUser.id()));

    assertEquals(0, this.repository.findAll(Filterable.emptySearch(),
        Pageable.ofSize(10)).getTotalElements());
  }
}