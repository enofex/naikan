package com.enofex.naikan.project;

import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.validator.DefaultValidator;
import java.net.URI;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @Operation(summary = "Update project")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Project successfully updated"),
          @ApiResponse(responseCode = "404", description = "Project is not found")
  })
  @PutMapping("/{id}")
  public ResponseEntity<Void> update(
          @Parameter(required = true, description = "Project ID") @PathVariable ProjectId id,
          @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                  description = "Project BOM request body", content = @Content(
                  schema = @Schema(implementation = Bom.class)
          )) @RequestBody Bom bom
  ) {
    if (this.projectService.findById(id).isPresent()) {
      this.projectService.update(id, bom);

      return ResponseEntity.ok().build();
    }

    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Upsert project by project name")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Project is already exists"),
          @ApiResponse(responseCode = "201", description = "Project successfully created",
                  headers = @Header(name = "Location", description = "Project URI")),
          @ApiResponse(responseCode = "400", description = "Request body is not valid")
  })
  @PostMapping
  public ResponseEntity<Void> upsertByProjectName(
          @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                  description = "Project BOM request body", content = @Content(
                  schema = @Schema(implementation = Bom.class)
          )) @RequestBody Bom bom
  ) {
    if (new DefaultValidator().isValid(bom)) {
      boolean existsAlready = this.projectService.existsByProjectName(bom.project().name());
      Bom newBom = this.projectService.upsertByProjectName(bom);

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
