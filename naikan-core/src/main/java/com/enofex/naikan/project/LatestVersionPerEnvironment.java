package com.enofex.naikan.project;

import com.enofex.naikan.model.Deployment;

public record LatestVersionPerEnvironment(String environment, Deployment deployment) {

}
