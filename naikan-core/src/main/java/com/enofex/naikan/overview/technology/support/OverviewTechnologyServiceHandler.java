package com.enofex.naikan.overview.technology.support;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.overview.OverviewTopGroups;
import com.enofex.naikan.overview.technology.OverviewTechnologyRepository;
import com.enofex.naikan.overview.technology.OverviewTechnologyService;
import com.enofex.naikan.overview.technology.TechnologyGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class OverviewTechnologyServiceHandler implements OverviewTechnologyService {

  private final OverviewTechnologyRepository overviewTechnologyRepository;

  OverviewTechnologyServiceHandler(OverviewTechnologyRepository overviewTechnologyRepository) {
    this.overviewTechnologyRepository = overviewTechnologyRepository;
  }

  @Override
  public Page<TechnologyGroup> findAll(Filterable filterable, Pageable pageable) {
    return this.overviewTechnologyRepository.findAll(filterable, pageable);
  }

  @Override
  public OverviewTopGroups findTopTechnologies(long topN) {
    return this.overviewTechnologyRepository.findTopTechnologies(topN);
  }
}
