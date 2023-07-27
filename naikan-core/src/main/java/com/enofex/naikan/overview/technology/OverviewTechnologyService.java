package com.enofex.naikan.overview.technology;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.overview.OverviewTopGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OverviewTechnologyService {

  Page<TechnologyGroup> findAll(Filterable filterable, Pageable pageable);

  OverviewTopGroups findTopTechnologies(long topN);

}
