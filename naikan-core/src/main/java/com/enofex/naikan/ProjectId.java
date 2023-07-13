package com.enofex.naikan;

public record ProjectId(String id) {

  public static ProjectId of(String id) {
    return new ProjectId(id);
  }
}
