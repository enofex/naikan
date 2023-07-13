package com.enofex.naikan.administration.token;

public record TokenId(String id) {

  public static TokenId of(String id) {
    return new TokenId(id);
  }
}