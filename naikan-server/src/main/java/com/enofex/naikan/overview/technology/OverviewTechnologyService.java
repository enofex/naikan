package com.enofex.naikan.overview.technology;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.overview.OverviewTopGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class OverviewTechnologyService {

  private final OverviewTechnologyRepository overviewTechnologyRepository;

  OverviewTechnologyService(OverviewTechnologyRepository overviewTechnologyRepository) {
    this.overviewTechnologyRepository = overviewTechnologyRepository;
  }

  public Page<TechnologyGroup> findAll(Filterable filterable, Pageable pageable) {
    return this.overviewTechnologyRepository.findAll(filterable, pageable);
  }

  public OverviewTopGroups findTopTechnologies(long topN) {
    return this.overviewTechnologyRepository.findTopTechnologies(topN);
  }
}
