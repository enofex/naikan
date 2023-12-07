package com.enofex.naikan.overview.technology;

import com.enofex.naikan.overview.OverviewBom;
import java.util.List;
import java.util.UUID;

record TechnologyGroup(String uuid, Group group, List<OverviewBom> boms, int count) {

  TechnologyGroup(String uuid, Group group, List<OverviewBom> boms, int count) {
    this.uuid = uuid != null ? uuid : UUID.randomUUID().toString();
    this.group = group;
    this.boms = boms != null ? List.copyOf(boms) : List.of();
    this.count = count;
  }

  record Group(String name, String version) {

  }

}
