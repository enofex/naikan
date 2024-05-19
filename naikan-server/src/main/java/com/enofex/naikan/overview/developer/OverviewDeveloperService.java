package com.enofex.naikan.overview.developer;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class OverviewDeveloperService {

  private final OverviewDeveloperRepository overviewDeveloperRepository;

  OverviewDeveloperService(OverviewDeveloperRepository overviewDeveloperRepository) {
    this.overviewDeveloperRepository = overviewDeveloperRepository;
  }

  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    return this.overviewDeveloperRepository.findAll(filterable, pageable);
  }
}
