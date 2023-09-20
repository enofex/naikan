package com.enofex.naikan.restapi.project.deployment;

import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;

public interface ApiDeploymentService {

  Bom save(ProjectId id, Deployment deployment);

}
