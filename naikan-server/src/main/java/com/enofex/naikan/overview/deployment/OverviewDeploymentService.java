package com.enofex.naikan.overview.deployment;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.overview.OverviewTopGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class OverviewDeploymentService {

  private final OverviewDeploymentRepository overviewDeploymentRepository;

  OverviewDeploymentService(OverviewDeploymentRepository overviewDeploymentRepository) {
    this.overviewDeploymentRepository = overviewDeploymentRepository;
  }


  public Page<OverviewDeployment> findAll(Filterable filterable, Pageable pageable) {
    return this.overviewDeploymentRepository.findAll(filterable, pageable);
  }


  public OverviewTopGroups findTopProjects(long topN) {
    return this.overviewDeploymentRepository.findTopProjects(topN);
  }
}
