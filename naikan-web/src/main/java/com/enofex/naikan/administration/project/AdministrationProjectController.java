package com.enofex.naikan.administration.project;

import static com.enofex.naikan.administration.project.AdministrationProjectController.REQUEST_PATH;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.ProjectId;
import com.enofex.naikan.administration.AdministrationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(REQUEST_PATH)
class AdministrationProjectController {

  static final String REQUEST_PATH = AdministrationRequest.PATH + "/projects";

  private final AdministrationProjectService administrationProjectService;

  AdministrationProjectController(AdministrationProjectService administrationProjectService) {
    this.administrationProjectService = administrationProjectService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Project>> findAll(Filterable filterable, Pageable pageable) {
    return ResponseEntity.ok(this.administrationProjectService.findAll(filterable, pageable));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable ProjectId id) {
    return this.administrationProjectService.deleteById(id) > 0
        ? ResponseEntity.ok().build()
        : ResponseEntity.notFound().build();
  }
}
