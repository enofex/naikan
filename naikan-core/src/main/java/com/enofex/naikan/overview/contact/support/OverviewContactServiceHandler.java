package com.enofex.naikan.overview.contact.support;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.contact.OverviewContactRepository;
import com.enofex.naikan.overview.contact.OverviewContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class OverviewContactServiceHandler implements OverviewContactService {

  private final OverviewContactRepository overviewContactRepository;

  OverviewContactServiceHandler(OverviewContactRepository overviewContactRepository) {
    this.overviewContactRepository = overviewContactRepository;
  }

  @Override
  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    return overviewContactRepository.findAll(filterable, pageable);
  }
}
