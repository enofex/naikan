package com.enofex.naikan.overview;

import java.util.List;
import java.util.UUID;

public record OverviewGroup(String uuid, Group group, List<OverviewBom> boms, int count) {

  public OverviewGroup(String uuid, Group group, List<OverviewBom> boms, int count) {
    this.uuid = uuid != null ? uuid : UUID.randomUUID().toString();
    this.group = group;
    this.boms = List.copyOf(boms);
    this.count = count;
  }

  public record Group(String name) {

  }

}
