package com.enofex.naikan.overview.developer;

import static com.enofex.naikan.overview.developer.OverviewDeveloperController.REQUEST_PATH;

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
class OverviewDeveloperController {

  static final String REQUEST_PATH = OverviewRequest.PATH + "/developers";

  private final OverviewDeveloperService overviewDeveloperService;

  OverviewDeveloperController(OverviewDeveloperService overviewDeveloperService) {
    this.overviewDeveloperService = overviewDeveloperService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<OverviewGroup>> findAll(Filterable filterable,
      Pageable pageable) {
    return ResponseEntity.ok(this.overviewDeveloperService.findAll(filterable, pageable));
  }
}
