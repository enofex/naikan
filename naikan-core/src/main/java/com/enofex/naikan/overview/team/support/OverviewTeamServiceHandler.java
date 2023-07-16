package com.enofex.naikan.overview.team.support;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.OverviewTopGroups;
import com.enofex.naikan.overview.team.OverviewTeamRepository;
import com.enofex.naikan.overview.team.OverviewTeamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class OverviewTeamServiceHandler implements OverviewTeamService {

  private final OverviewTeamRepository overviewTeamRepository;

  OverviewTeamServiceHandler(OverviewTeamRepository overviewTeamRepository) {
    this.overviewTeamRepository = overviewTeamRepository;
  }

  @Override
  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    return this.overviewTeamRepository.findAll(filterable, pageable);
  }

  @Override
  public OverviewTopGroups findTopTeams(long topN) {
    return this.overviewTeamRepository.findTopTeams(topN);
  }
}
