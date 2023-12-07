package com.enofex.naikan.security;

import static com.enofex.naikan.test.Users.user;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.enofex.naikan.test.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

@IntegrationTest
class UserRepositoryIT {

  @Autowired
  private MongoTemplate template;

  private UserRepository repository;

  @BeforeEach
  void setUp() {
    this.repository = new UserRepository(this.template);
  }

  @Test
  void shouldFindByName() {
    User savedUser = this.repository.save(user());

    assertEquals(savedUser.name(), this.repository.findByName(savedUser.name()).name());
  }

  @Test
  void shouldCount() {
    assertEquals(0, this.repository.count());

    this.repository.save(user());

    assertEquals(1, this.repository.count());
  }
}