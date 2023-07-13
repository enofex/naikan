package com.enofex.naikan.overview;

import java.util.List;

public record OverviewTopGroups(List<String> names, List<Long> counts) {

  public OverviewTopGroups(List<String> names, List<Long> counts) {
    this.names = List.copyOf(names);
    this.counts = List.copyOf(counts);
  }
}
