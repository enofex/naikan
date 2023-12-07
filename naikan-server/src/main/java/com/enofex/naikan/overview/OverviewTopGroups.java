package com.enofex.naikan.overview;

import java.util.List;

public record OverviewTopGroups(List<String> names, List<Long> counts) {

  public OverviewTopGroups {
    names = names != null ? List.copyOf(names) : List.of();
    counts = counts != null ? List.copyOf(counts) : List.of();
  }
}
