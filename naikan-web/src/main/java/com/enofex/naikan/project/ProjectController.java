package com.enofex.naikan.project;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.ProjectId;
import com.enofex.naikan.administration.user.AdministrationUserService;
import com.enofex.naikan.administration.user.User;
import com.enofex.naikan.model.Bom;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(ProjectRequest.PATH)
class ProjectController {

  private final ProjectService projectService;
  private final AdministrationUserService administrationUserService;

  ProjectController(ProjectService projectService,
      AdministrationUserService administrationUserService) {
    this.projectService = projectService;
    this.administrationUserService = administrationUserService;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Bom>> findAll(Filterable filterable, Pageable pageable) {
    return ResponseEntity.ok(this.projectService.findAll(filterable, pageable));
  }

  @GetMapping(params = "xlsx")
  public ModelAndView xlsx(Filterable filterable, Pageable pageable) {
    List<Bom> boms = this.projectService.findAll(filterable, pageable).getContent();
    return new ModelAndView(new ProjectXlsxView(boms));
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Bom> findById(@PathVariable ProjectId id) {
    return this.projectService.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProjectFilter> findFilter() {
    return ResponseEntity.ok(this.projectService.findFilter());
  }

  @PatchMapping("/favorites")
  public ResponseEntity<Void> updateUserFavorites(@RequestBody String[] favorites,
      Authentication authentication) {
    User user = this.administrationUserService.findByName(authentication.getName());

    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    this.administrationUserService.save(User.ofFavorites(user, favorites));

    return ResponseEntity.noContent().build();
  }
}
