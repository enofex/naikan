package com.enofex.naikan.web;

public record ProjectId(String id) {

  public static ProjectId of(String id) {
    return new ProjectId(id);
  }
}
