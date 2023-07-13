package com.enofex.naikan.overview.integration;

import static com.enofex.naikan.overview.integration.OverviewIntegrationController.REQUEST_PATH;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.OverviewRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(REQUEST_PATH)
class OverviewIntegrationController {

  static final String REQUEST_PATH = OverviewRequest.PATH + "/integrations";

  private final OverviewIntegrationService overviewIntegrationService;

  OverviewIntegrationController(OverviewIntegrationService overviewIntegrationService) {
    this.overviewIntegrationService = overviewIntegrationService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<OverviewGroup>> findAll(Filterable filterable,
      Pageable pageable) {
    return ResponseEntity.ok(
        this.overviewIntegrationService.findAll(filterable, pageable));
  }
}
