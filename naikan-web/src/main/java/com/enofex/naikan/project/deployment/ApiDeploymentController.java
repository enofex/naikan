package com.enofex.naikan.project.deployment;

import static com.enofex.naikan.project.deployment.ApiDeploymentController.REQUEST_PATH;

import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.project.ApiProjectRequest;
import com.enofex.naikan.project.ProjectRequest;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(REQUEST_PATH)
class ApiDeploymentController {

  static final String REQUEST_PATH = ApiProjectRequest.PATH_WITH_ID + "/deployments";

  private final DeploymentService deploymentService;

  ApiDeploymentController(DeploymentService deploymentService) {
    this.deploymentService = deploymentService;
  }

  @Operation(summary = "Save new deployment in the given project")
  @ApiResponses({
      @ApiResponse(responseCode = "201", description = "Deployment successfully created",
          headers = @Header(name = "Location", description = "Deployment URI")),
      @ApiResponse(responseCode = "404", description = "Project not found")
  })
  @PostMapping
  public ResponseEntity<String> save(
      @Parameter(required = true, description = "Project ID") @PathVariable ProjectId id,
      @io.swagger.v3.oas.annotations.parameters.RequestBody(
          required = true,
          description = "Deployment request body",
          content = @Content(schema = @Schema(implementation = Deployment.class))
      ) @RequestBody Deployment deployment
  ) {
    Bom newBom = this.deploymentService.save(id, deployment);

    if (newBom != null) {
      URI location = ServletUriComponentsBuilder
          .fromCurrentRequest().replacePath(ProjectRequest.PATH_WITH_ID + "/deployments/{index}")
          .buildAndExpand(newBom.id(), newBom.deployments().lastIndex())
          .toUri();

      return ResponseEntity.created(location).build();
    }

    return ResponseEntity.notFound().build();
  }
}
