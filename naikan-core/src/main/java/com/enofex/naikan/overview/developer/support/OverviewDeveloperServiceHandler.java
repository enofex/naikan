package com.enofex.naikan.overview.developer.support;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.developer.OverviewDeveloperRepository;
import com.enofex.naikan.overview.developer.OverviewDeveloperService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class OverviewDeveloperServiceHandler implements OverviewDeveloperService {

  private final OverviewDeveloperRepository overviewDeveloperRepository;

  OverviewDeveloperServiceHandler(OverviewDeveloperRepository overviewDeveloperRepository) {
    this.overviewDeveloperRepository = overviewDeveloperRepository;
  }

  @Override
  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    return this.overviewDeveloperRepository.findAll(filterable, pageable);
  }
}
