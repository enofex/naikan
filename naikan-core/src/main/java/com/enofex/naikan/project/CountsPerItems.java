package com.enofex.naikan.project;

import java.util.List;

public record CountsPerItems(List<String> names, List<Long> counts) {

  public static final CountsPerItems EMPTY = new CountsPerItems(List.of(), List.of());

  public CountsPerItems {
    names = names != null ? List.copyOf(names) : List.of();
    counts = counts != null ? List.copyOf(counts) : List.of();
  }
}
