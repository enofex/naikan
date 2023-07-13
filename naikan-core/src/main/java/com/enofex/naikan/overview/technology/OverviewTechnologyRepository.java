package com.enofex.naikan.overview.technology;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.OverviewTopGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OverviewTechnologyRepository {

  Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable);

  OverviewTopGroups findTopTechnologies();

}
