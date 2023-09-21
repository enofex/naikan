package com.enofex.naikan.project;

import java.util.List;

public record DeploymentsPerProject(List<String> projects, List<Long> counts) {

  public DeploymentsPerProject {
    projects = projects != null ? List.copyOf(projects) : List.of();
    counts = counts != null ? List.copyOf(counts) : List.of();
  }
}
