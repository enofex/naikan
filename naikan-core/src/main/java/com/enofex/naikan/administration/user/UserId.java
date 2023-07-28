package com.enofex.naikan.administration.user;

public record UserId(String id) {

  public static UserId of(String id) {
    return new UserId(id);
  }
}