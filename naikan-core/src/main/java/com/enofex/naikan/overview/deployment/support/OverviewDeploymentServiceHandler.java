package com.enofex.naikan.overview.deployment.support;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.overview.OverviewTopGroups;
import com.enofex.naikan.overview.deployment.OverviewDeployment;
import com.enofex.naikan.overview.deployment.OverviewDeploymentRepository;
import com.enofex.naikan.overview.deployment.OverviewDeploymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class OverviewDeploymentServiceHandler implements OverviewDeploymentService {

  private final OverviewDeploymentRepository overviewDeploymentRepository;

  OverviewDeploymentServiceHandler(OverviewDeploymentRepository overviewDeploymentRepository) {
    this.overviewDeploymentRepository = overviewDeploymentRepository;
  }

  @Override
  public Page<OverviewDeployment> findAll(Filterable filterable, Pageable pageable) {
    return this.overviewDeploymentRepository.findAll(filterable, pageable);
  }

  @Override
  public OverviewTopGroups findTopProjects(long topN) {
    return this.overviewDeploymentRepository.findTopProjects(topN);
  }
}
