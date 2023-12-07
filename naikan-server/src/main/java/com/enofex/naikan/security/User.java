package com.enofex.naikan.security;


import java.util.List;

public record User(String id, String name, List<String> authorities, List<String> favorites) {

  public User(String id, String name, List<String> authorities, List<String> favorites) {
    this.id = id;
    this.name = name;
    this.authorities = authorities != null ? List.copyOf(authorities) : List.of();
    this.favorites = favorites != null ? List.copyOf(favorites) : List.of();
  }

  public User(String name, List<String> authorities) {
    this(null, name, authorities, List.of());
  }

  public User(String name) {
    this(null, name, List.of(), List.of());
  }

  public static User ofAuthorities(User user, String[] authorities) {
    return new User(
        user.id(),
        user.name(),
        authorities != null ? List.of(authorities) : List.of(),
        List.of());
  }

  public static User ofFavorites(User user, String[] favorites) {
    return new User(
        user.id(),
        user.name(),
        user.authorities(),
        favorites != null ? List.of(favorites) : List.of());
  }
}
