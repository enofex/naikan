package com.enofex.naikan.project;

import java.util.List;

public record DeploymentsPerMonth(List<String> months, List<Long> counts) {

  public DeploymentsPerMonth {
    months = months != null ? List.copyOf(months) : List.of();
    counts = counts != null ? List.copyOf(counts) : List.of();
  }
}
