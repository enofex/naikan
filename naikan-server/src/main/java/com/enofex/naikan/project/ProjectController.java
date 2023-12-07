package com.enofex.naikan.project;

import com.enofex.naikan.model.Branch;
import com.enofex.naikan.model.Commit;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.model.RepositoryTag;
import com.enofex.naikan.security.User;
import com.enofex.naikan.security.UserRepository;
import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.web.ProjectId;
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
  private final UserRepository userRepository;

  ProjectController(ProjectService projectService, UserRepository userRepository) {
    this.projectService = projectService;
    this.userRepository = userRepository;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<BomOverview>> findAll(Filterable filterable, Pageable pageable) {
    return ResponseEntity.ok(this.projectService.findAll(filterable, pageable));
  }

  @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProjectFilter> findFilter() {
    return ResponseEntity.ok(this.projectService.findFilter());
  }

  @PatchMapping("/favorites")
  public ResponseEntity<Void> updateUserFavorites(@RequestBody String[] favorites,
      Authentication authentication) {
    User user = this.userRepository.findByName(authentication.getName());

    if (user == null) {
      return ResponseEntity.notFound().build();
    }

    this.userRepository.save(User.ofFavorites(user, favorites));

    return ResponseEntity.noContent().build();
  }

  @GetMapping(params = "xlsx")
  public ModelAndView xlsxAll(Filterable filterable, Pageable pageable) {
    List<BomOverview> boms = this.projectService.findAll(filterable, pageable).getContent();
    return new ModelAndView(new ProjectAllXlsxView(boms));
  }

  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<BomDetail> findBomDetailById(@PathVariable ProjectId id) {
    return this.projectService.findBomDetailById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  @GetMapping(value = "/{id}/deployments", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Deployment>> findDeploymentsById(@PathVariable ProjectId id,
      Filterable filterable, Pageable pageable) {
    return ResponseEntity.ok(this.projectService.findDeploymentsById(id, filterable, pageable));
  }

  @GetMapping(value = "/{id}/deployments/months", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CountsPerItems> findDeploymentsPerMonthById(
      @PathVariable ProjectId id) {
    return ResponseEntity.ok(this.projectService.findDeploymentsPerMonthById(id));
  }

  @GetMapping(value = "/deployments/months", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CountsPerItems> findDeploymentsPerMonth(Filterable filterable,
      Pageable pageable) {
    return ResponseEntity.ok(
        this.projectService.findDeploymentsPerMonth(filterable, pageable));
  }

  @GetMapping(value = "/deployments/projects", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CountsPerItems> findDeploymentsPerProject(Filterable filterable,
      Pageable pageable) {
    return ResponseEntity.ok(this.projectService.findDeploymentsPerProject(filterable, pageable));
  }

  @GetMapping(value = "/{id}/versions/grouped", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<GroupedDeploymentsPerVersion>> findGroupedDeploymentsPerVersionById(
      @PathVariable ProjectId id, Filterable filterable, Pageable pageable) {
    return ResponseEntity.ok(
        this.projectService.findGroupedDeploymentsPerVersionById(id, filterable, pageable));
  }

  @GetMapping(value = "/{id}/versions/environments", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<LatestVersionPerEnvironment>> findLatestVersionPerEnvironmentById(
      @PathVariable ProjectId id) {
    return ResponseEntity.ok(this.projectService.findLatestVersionPerEnvironmentById(id));
  }

  @GetMapping(value = "/{id}/commits", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Commit>> findCommitsById(@PathVariable ProjectId id,
      Filterable filterable, Pageable pageable) {
    return ResponseEntity.ok(this.projectService.findCommitsById(id, filterable, pageable));
  }

  @GetMapping(value = "/{id}/commits/months", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CountsPerItems> findCommitsPerMonthById(@PathVariable ProjectId id) {
    return ResponseEntity.ok(this.projectService.findCommitsPerMonthById(id));
  }

  @GetMapping(value = "/commits/months", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CountsPerItems> findCommitsPerMonth(Filterable filterable,
      Pageable pageable) {
    return ResponseEntity.ok(
        this.projectService.findCommitsPerMonth(filterable, pageable));
  }

  @GetMapping(value = "/commits/projects", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<CountsPerItems> findCommitsPerProject(Filterable filterable,
      Pageable pageable) {
    return ResponseEntity.ok(this.projectService.findCommitsPerProject(filterable, pageable));
  }

  @GetMapping(value = "/{id}/repository/tags", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<RepositoryTag>> findRepositoryTagsById(@PathVariable ProjectId id,
      Filterable filterable, Pageable pageable) {
    return ResponseEntity.ok(this.projectService.findRepositoryTagsById(id, filterable, pageable));
  }

  @GetMapping(value = "/{id}/repository/branches", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Page<Branch>> findRepositoryBranchesById(
      @PathVariable ProjectId id,
      Filterable filterable, Pageable pageable) {
    return ResponseEntity.ok(
        this.projectService.findRepositoryBranchesById(id, filterable, pageable));
  }

  @GetMapping(params = "xlsx", value = "/{id}")
  public ModelAndView xlsxById(@PathVariable ProjectId id) {
    return this.projectService.findById(id)
        .map(bom -> new ModelAndView(new ProjectXlsxView(bom)))
        .orElseGet(ModelAndView::new);
  }
}
