package com.enofex.naikan.overview.team;

import static com.enofex.naikan.overview.team.OverviewTeamController.REQUEST_PATH;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.OverviewRequest;
import com.enofex.naikan.overview.OverviewTopGroups;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(REQUEST_PATH)
class OverviewTeamController {

  static final String REQUEST_PATH = OverviewRequest.PATH + "/teams";

  private final OverviewTeamService overviewTeamService;

  OverviewTeamController(OverviewTeamService overviewTeamService) {
    this.overviewTeamService = overviewTeamService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<OverviewGroup>> findAll(Filterable filterable, Pageable pageable) {
    return ResponseEntity.ok(this.overviewTeamService.findAll(filterable, pageable));
  }

  @GetMapping(path = "/top/{topN}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OverviewTopGroups> findTopTeams(@PathVariable long topN) {
    return ResponseEntity.ok(this.overviewTeamService.findTopTeams(topN));
  }
}
