package com.enofex.naikan.architecture;

import com.enofex.naikan.test.architecture.ArchUnitTestsConfig;
import java.util.Collection;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class ArchitectureTest {

  @TestFactory
  Collection<DynamicTest> shouldFulfilArchitectureConstrains() {
    ArchUnitTestsConfig config = ArchUnitTestsConfig.defaultConfig();

    config.addDynamicTests(ServiceRules.serviceRules(config));
    config.addDynamicTests(RepositoryRules.repositoryRules(config));

    return config.getDynamicTests();
  }
}
