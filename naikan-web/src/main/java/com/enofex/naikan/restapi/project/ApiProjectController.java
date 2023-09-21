package com.enofex.naikan.restapi.project;

import com.enofex.naikan.ProjectId;
import com.enofex.naikan.restapi.ApiProjectRequest;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.validator.DefaultValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  private final ApiProjectService apiProjectService;

  ApiProjectController(ApiProjectService apiProjectService) {
    this.apiProjectService = apiProjectService;
  }

  @Operation(summary = "Update project")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Project successfully updated"),
      @ApiResponse(responseCode = "404", description = "Project not found")
  })
  @PutMapping("/{id}")
  public ResponseEntity<Void> updateById(
      @Parameter(required = true, description = "Project ID") @PathVariable ProjectId id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          description = "Project BOM request body",
          content = @Content(schema = @Schema(implementation = Bom.class))
      ) @RequestBody Bom bom
  ) {
    if (this.apiProjectService.findById(id).isPresent()) {
      this.apiProjectService.updateById(id, bom);

      return ResponseEntity.ok().build();
    }

    return ResponseEntity.notFound().build();
  }

  @Operation(summary = "Upsert project by project name")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Project already exists"),
      @ApiResponse(responseCode = "201", description = "Project successfully created",
          headers = @Header(name = "Location", description = "Project URI")),
      @ApiResponse(responseCode = "400", description = "Request body not valid")
  })
  @PostMapping
  public ResponseEntity<Void> upsertByProjectName(
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          description = "Project BOM request body",
          content = @Content(schema = @Schema(implementation = Bom.class))
      ) @RequestBody Bom bom
  ) {
    if (new DefaultValidator().isValid(bom)) {
      boolean existsAlready = this.apiProjectService.existsByProjectName(bom.project().name());
      Bom newBom = this.apiProjectService.upsertByProjectName(bom);

      if (existsAlready) {
        return ResponseEntity.ok().build();
      }

      URI location = ServletUriComponentsBuilder
          .fromCurrentRequest()
          .replacePath(ApiProjectRequest.PATH_WITH_ID)
          .buildAndExpand(newBom.id())
          .toUri();

      return ResponseEntity.created(location).build();
    }

    return ResponseEntity.badRequest().build();
  }
}
