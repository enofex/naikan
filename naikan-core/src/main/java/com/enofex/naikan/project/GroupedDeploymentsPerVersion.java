package com.enofex.naikan.project;

import com.enofex.naikan.model.Deployment;
import java.util.List;

public record GroupedDeploymentsPerVersion(String version, int count,
                                           List<Deployment> deployments) {

}