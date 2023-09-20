package com.enofex.naikan.restapi.project.deployment.support;

import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.restapi.project.deployment.ApiDeploymentRepository;
import com.enofex.naikan.restapi.project.deployment.ApiDeploymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class ApiDeploymentServiceHandler implements ApiDeploymentService {

  private final ApiDeploymentRepository apiDeploymentRepository;

  ApiDeploymentServiceHandler(ApiDeploymentRepository apiDeploymentRepository) {
    this.apiDeploymentRepository = apiDeploymentRepository;
  }

  @Override
  public Bom save(ProjectId id, Deployment deployment) {
    return this.apiDeploymentRepository.save(id, deployment);
  }
}
