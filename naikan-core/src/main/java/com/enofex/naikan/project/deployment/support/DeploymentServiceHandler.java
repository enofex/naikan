package com.enofex.naikan.project.deployment.support;

import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.project.deployment.DeploymentRepository;
import com.enofex.naikan.project.deployment.DeploymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class DeploymentServiceHandler implements DeploymentService {

  private final DeploymentRepository deploymentRepository;

  DeploymentServiceHandler(DeploymentRepository deploymentRepository) {
    this.deploymentRepository = deploymentRepository;
  }

  @Override
  public Bom save(ProjectId id, Deployment deployment) {
    return deploymentRepository.save(id, deployment);
  }
}
