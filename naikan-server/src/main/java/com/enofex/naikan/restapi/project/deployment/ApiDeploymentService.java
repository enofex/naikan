package com.enofex.naikan.restapi.project.deployment;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.web.ProjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class ApiDeploymentService {

  private final ApiDeploymentRepository apiDeploymentRepository;

  ApiDeploymentService(ApiDeploymentRepository apiDeploymentRepository) {
    this.apiDeploymentRepository = apiDeploymentRepository;
  }


  public Bom saveById(ProjectId id, Deployment deployment) {
    return this.apiDeploymentRepository.saveById(id, deployment);
  }
}
