package com.enofex.naikan.administration.user.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.administration.user.AdministrationUserRepository;
import com.enofex.naikan.administration.user.User;
import com.enofex.naikan.administration.user.UserId;
import com.enofex.naikan.test.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

@IntegrationTest
class AdministrationUserMongoRepositoryIT {

  @Autowired
  private MongoTemplate template;

  private AdministrationUserRepository repository;

  @BeforeEach
  void setUp() {
    this.repository = new AdministrationUserMongoRepository(this.template);
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
  void shouldFindByName() {
    User savedUser = this.repository.save(user());

    assertEquals(savedUser.name(), this.repository.findByName(savedUser.name()).name());
  }

  @Test
  void shouldDelete() {
    User savedUser = this.repository.save(user());

    assertEquals(1, this.repository.findAll(Filterable.emptySearch(),
        Pageable.ofSize(10)).getTotalElements());

    this.repository.delete(UserId.of(savedUser.id()));

    assertEquals(0, this.repository.findAll(Filterable.emptySearch(),
        Pageable.ofSize(10)).getTotalElements());
  }

  private static User user() {
    return new User("user@company.com");
  }
}