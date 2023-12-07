package com.enofex.naikan.test;

import com.enofex.naikan.security.User;

public final class Users {

  private Users() {
  }

  public static User user() {
    return new User("user@company.com");
  }

}
