package com.enofex.naikan.overview.technology;

import com.enofex.naikan.overview.OverviewBom;
import java.util.List;
import java.util.UUID;

public record TechnologyGroup(String uuid, Group group, List<OverviewBom> boms, int count) {

  public TechnologyGroup(String uuid, Group group, List<OverviewBom> boms, int count) {
    this.uuid = uuid != null ? uuid : UUID.randomUUID().toString();
    this.group = group;
    this.boms = List.copyOf(boms);
    this.count = count;
  }

  public record Group(String name, String version) {

  }

}
