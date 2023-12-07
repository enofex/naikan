package com.enofex.naikan.overview.team;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.OverviewTopGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class OverviewTeamService {

  private final OverviewTeamRepository overviewTeamRepository;

  OverviewTeamService(OverviewTeamRepository overviewTeamRepository) {
    this.overviewTeamRepository = overviewTeamRepository;
  }

  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    return this.overviewTeamRepository.findAll(filterable, pageable);
  }

  public OverviewTopGroups findTopTeams(long topN) {
    return this.overviewTeamRepository.findTopTeams(topN);
  }
}
