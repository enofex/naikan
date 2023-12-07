package com.enofex.naikan.administration.token;

record TokenId(String id) {

  static TokenId of(String id) {
    return new TokenId(id);
  }
}