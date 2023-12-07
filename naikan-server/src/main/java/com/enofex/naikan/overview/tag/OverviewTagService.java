package com.enofex.naikan.overview.tag;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.OverviewTopGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class OverviewTagService {

  private final OverviewTagRepository overviewTagRepository;

  OverviewTagService(OverviewTagRepository overviewTagRepository) {
    this.overviewTagRepository = overviewTagRepository;
  }

  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    return this.overviewTagRepository.findAll(filterable, pageable);
  }

  public OverviewTopGroups findTopTags(long topN) {
    return this.overviewTagRepository.findTopTags(topN);
  }

}
