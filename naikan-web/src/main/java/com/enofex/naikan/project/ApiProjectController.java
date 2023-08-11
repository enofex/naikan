package com.enofex.naikan.project;

import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.validator.DefaultValidator;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(ApiProjectRequest.PATH)
class ApiProjectController {

  private final ProjectService projectService;

  ApiProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> update(@PathVariable ProjectId id, @RequestBody Bom bom) {
    if (projectService.findById(id).isPresent()) {
      projectService.update(id, bom);

      return ResponseEntity.ok().build();
    }

    return ResponseEntity.notFound().build();
  }

  @PostMapping
  public ResponseEntity<Void> upsertByProjectName(@RequestBody Bom bom) {
    if (new DefaultValidator().isValid(bom)) {
      boolean existsAlready = projectService.existsByProjectName(bom.project().name());
      Bom newBom = projectService.upsertByProjectName(bom);

      if (existsAlready) {
        return ResponseEntity.ok().build();
      }

      URI location = ServletUriComponentsBuilder
          .fromCurrentRequest()
          .replacePath(ProjectRequest.PATH_WITH_ID)
          .buildAndExpand(newBom.id())
          .toUri();

      return ResponseEntity.created(location).build();
    }

    return ResponseEntity.badRequest().build();
  }
}
