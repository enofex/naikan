package com.enofex.naikan.overview.integration.support;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.integration.OverviewIntegrationRepository;
import com.enofex.naikan.overview.integration.OverviewIntegrationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class OverviewIntegrationServiceHandler implements OverviewIntegrationService {

  private final OverviewIntegrationRepository overviewIntegrationRepository;

  OverviewIntegrationServiceHandler(OverviewIntegrationRepository overviewIntegrationRepository) {
    this.overviewIntegrationRepository = overviewIntegrationRepository;
  }

  @Override
  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    return this.overviewIntegrationRepository.findAll(filterable, pageable);
  }
}
