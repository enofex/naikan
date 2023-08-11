package com.enofex.naikan.overview.tag.support;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.OverviewTopGroups;
import com.enofex.naikan.overview.tag.OverviewTagRepository;
import com.enofex.naikan.overview.tag.OverviewTagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class OverviewTagServiceHandler implements OverviewTagService {

  private final OverviewTagRepository overviewTagRepository;

  OverviewTagServiceHandler(OverviewTagRepository overviewTagRepository) {
    this.overviewTagRepository = overviewTagRepository;
  }

  @Override
  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    return overviewTagRepository.findAll(filterable, pageable);
  }

  @Override
  public OverviewTopGroups findTopTags(long topN) {
    return overviewTagRepository.findTopTags(topN);
  }

}
