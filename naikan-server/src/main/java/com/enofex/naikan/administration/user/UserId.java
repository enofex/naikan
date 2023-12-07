package com.enofex.naikan.administration.user;

record UserId(String id) {

  static UserId of(String id) {
    return new UserId(id);
  }
}