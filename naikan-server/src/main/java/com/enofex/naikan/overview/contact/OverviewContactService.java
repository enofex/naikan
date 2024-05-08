package com.enofex.naikan.overview.contact;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class OverviewContactService {

  private final OverviewContactRepository overviewContactRepository;

  OverviewContactService(OverviewContactRepository overviewContactRepository) {
    this.overviewContactRepository = overviewContactRepository;
  }

  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    return this.overviewContactRepository.findAll(filterable, pageable);
  }
}
