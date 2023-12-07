package com.enofex.naikan.overview.deployment;

import static com.enofex.naikan.overview.deployment.OverviewDeploymentController.REQUEST_PATH;

import com.enofex.naikan.web.Filterable;
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
class OverviewDeploymentController {

  static final String REQUEST_PATH = OverviewRequest.PATH + "/deployments";

  private final OverviewDeploymentService overviewDeploymentService;

  OverviewDeploymentController(OverviewDeploymentService overviewDeploymentService) {
    this.overviewDeploymentService = overviewDeploymentService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<OverviewDeployment>> findAll(Filterable filterable,
      Pageable pageable) {
    return ResponseEntity.ok(this.overviewDeploymentService.findAll(filterable, pageable));
  }

  @GetMapping(path = "/top/{topN}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<OverviewTopGroups> findTopProjects(@PathVariable long topN) {
    return ResponseEntity.ok(this.overviewDeploymentService.findTopProjects(topN));
  }
}
