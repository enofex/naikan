package com.enofex.naikan.project;

import com.enofex.naikan.model.Deployment;
import java.util.List;

record GroupedDeploymentsPerVersion(String version, int count, List<Deployment> deployments) {

  GroupedDeploymentsPerVersion {
    deployments = deployments != null ? deployments : List.of();
  }

}