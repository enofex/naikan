package com.enofex.naikan.security;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UserTest {

  private User user;

  @BeforeEach
  void setUp() {
    this.user = new User("123", "John Doe", List.of("ROLE_USER"), List.of("Book1", "Book2"));
  }

  @Test
  void shouldCreateUserWithIdNameAuthoritiesAndFavorites() {
    assertAll(
        () -> assertEquals("123", this.user.id()),
        () -> assertEquals("John Doe", this.user.name()),
        () -> assertEquals(List.of("ROLE_USER"), this.user.authorities()),
        () -> assertEquals(List.of("Book1", "Book2"), this.user.favorites())
    );
  }

  @Test
  void shouldCreateUserWithNameAndAuthorities() {
    User user = new User("John Doe", List.of("ROLE_ADMIN"));

    assertAll(
        () -> assertNull(user.id()),
        () -> assertEquals("John Doe", user.name()),
        () -> assertEquals(List.of("ROLE_ADMIN"), user.authorities()),
        () -> assertEquals(List.of(), user.favorites())
    );
  }

  @Test
  void shouldCreateUserWithName() {
    User user = new User("Jane Smith");

    assertAll(
        () -> assertNull(user.id()),
        () -> assertEquals("Jane Smith", user.name()),
        () -> assertEquals(List.of(), user.authorities()),
        () -> assertEquals(List.of(), user.favorites())
    );
  }

  @Test
  void shouldCreateUserWithAuthorities() {
    User user = User.ofAuthorities(this.user, new String[]{"ROLE_ADMIN", "ROLE_EDITOR"});

    assertAll(
        () -> assertEquals("123", user.id()),
        () -> assertEquals("John Doe", user.name()),
        () -> assertEquals(List.of("ROLE_ADMIN", "ROLE_EDITOR"), user.authorities()),
        () -> assertEquals(List.of(), user.favorites())
    );
  }

  @Test
  void shouldCreateUserWithFavorites() {
    User user = User.ofFavorites(this.user, new String[]{"Book3", "Book4"});

    assertAll(
        () -> assertEquals("123", user.id()),
        () -> assertEquals("John Doe", user.name()),
        () -> assertEquals(List.of("ROLE_USER"), user.authorities()),
        () -> assertEquals(List.of("Book3", "Book4"), user.favorites())
    );
  }

  @Test
  void shouldCreateUserWithNullAuthorities() {
    User user = User.ofAuthorities(this.user, null);

    assertAll(
        () -> assertEquals("123", user.id()),
        () -> assertEquals("John Doe", user.name()),
        () -> assertEquals(List.of(), user.authorities()),
        () -> assertEquals(List.of(), user.favorites())
    );
  }

  @Test
  void shouldCreateUserWithNullFavorites() {
    User user = User.ofFavorites(this.user, null);

    assertAll(
        () -> assertEquals("123", user.id()),
        () -> assertEquals("John Doe", user.name()),
        () -> assertEquals(List.of("ROLE_USER"), user.authorities()),
        () -> assertEquals(List.of(), user.favorites())
    );
  }
}
