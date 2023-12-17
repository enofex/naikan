package com.enofex.naikan.architecture;

import static com.enofex.naikan.architecture.ControllerRules.controllerRules;
import static com.enofex.naikan.architecture.RepositoryRules.repositoryRules;
import static com.enofex.naikan.architecture.ServiceRules.serviceRules;
import static com.enofex.naikan.architecture.SpringRules.springBootRules;

import com.enofex.naikan.test.architecture.ArchUnitTestsConfig;
import java.util.Collection;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class ArchitectureTest {

  @TestFactory
  Collection<DynamicTest> shouldFulfilArchitectureConstrains() {
    ArchUnitTestsConfig config = ArchUnitTestsConfig.defaultConfig();

    config.addDynamicTests(springBootRules(config));
    config.addDynamicTests(controllerRules(config));
    config.addDynamicTests(serviceRules(config));
    config.addDynamicTests(repositoryRules(config));

    return config.getDynamicTests();
  }
}
