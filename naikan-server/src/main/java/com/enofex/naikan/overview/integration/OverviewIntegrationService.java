package com.enofex.naikan.overview.integration;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class OverviewIntegrationService {

  private final OverviewIntegrationRepository overviewIntegrationRepository;

  OverviewIntegrationService(OverviewIntegrationRepository overviewIntegrationRepository) {
    this.overviewIntegrationRepository = overviewIntegrationRepository;
  }

  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    return this.overviewIntegrationRepository.findAll(filterable, pageable);
  }
}
