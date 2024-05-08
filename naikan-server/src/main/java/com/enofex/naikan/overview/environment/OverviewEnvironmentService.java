package com.enofex.naikan.overview.environment;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.OverviewTopGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class OverviewEnvironmentService {

  private final OverviewEnvironmentRepository overviewEnvironmentRepository;

  OverviewEnvironmentService(OverviewEnvironmentRepository overviewEnvironmentRepository) {
    this.overviewEnvironmentRepository = overviewEnvironmentRepository;
  }

  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    return this.overviewEnvironmentRepository.findAll(filterable, pageable);
  }

  public OverviewTopGroups findTopEnvironments(long topN) {
    return this.overviewEnvironmentRepository.findTopEnvironments(topN);
  }

}
