package com.enofex.naikan.overview.deployment;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.overview.OverviewTopGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OverviewDeploymentService {

  Page<OverviewDeployment> findAll(Filterable filterable, Pageable pageable);

  OverviewTopGroups findTopProjects();
}
