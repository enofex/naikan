package com.enofex.naikan.project;

import com.enofex.naikan.model.Deployment;

record LatestVersionPerEnvironment(String environment, Deployment deployment) {

}
