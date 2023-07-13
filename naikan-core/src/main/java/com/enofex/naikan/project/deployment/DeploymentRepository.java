package com.enofex.naikan.project.deployment;

import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;

public interface DeploymentRepository {

  Bom save(ProjectId id, Deployment deployment);

}
