package com.enofex.naikan.administration.user;

public final class UserId {

  private final String id;

  UserId(String id) {
    this.id = id;
  }

  public String id() {
    return this.id;
  }

  public static UserId of(String id) {
    return new UserId(id);
  }
}