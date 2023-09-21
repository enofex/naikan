package com.enofex.naikan.overview;

import java.util.List;
import java.util.UUID;

public record OverviewGroup(String uuid, Group group, List<OverviewBom> boms, int count) {

  public OverviewGroup {
    uuid = uuid != null ? uuid : UUID.randomUUID().toString();
    boms = boms != null ? List.copyOf(boms) : List.of();
  }

  public record Group(String name) {

  }

}
