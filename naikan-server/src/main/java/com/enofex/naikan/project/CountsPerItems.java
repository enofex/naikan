package com.enofex.naikan.project;

import java.util.List;

record CountsPerItems(List<String> names, List<Long> counts) {

  static final CountsPerItems EMPTY = new CountsPerItems(List.of(), List.of());

  CountsPerItems {
    names = names != null ? List.copyOf(names) : List.of();
    counts = counts != null ? List.copyOf(counts) : List.of();
  }
}
