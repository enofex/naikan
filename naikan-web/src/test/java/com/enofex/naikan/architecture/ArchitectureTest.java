package com.enofex.naikan.architecture;

import static com.enofex.naikan.architecture.ControllerRules.controllerRules;

import com.enofex.naikan.test.architecture.ArchUnitTestsConfig;
import java.util.Collection;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class ArchitectureTest {

  @TestFactory
  Collection<DynamicTest> shouldFulfilArchitectureConstrains() {
    ArchUnitTestsConfig config = ArchUnitTestsConfig.defaultConfig();

    config.addDynamicTests(controllerRules(config));

    return config.getDynamicTests();
  }
}
