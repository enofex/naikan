package com.enofex.naikan.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.enofex.naikan.test.architecture.ArchUnitTestsConfig;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.springframework.context.annotation.Configuration;

final class ConfigurationRules {

  private ConfigurationRules() {
  }

  static List<DynamicTest> configurationRules(ArchUnitTestsConfig config) {
    return List.of(
        dynamicTest("Configurations should be named *Configuration",
            () -> classes().that()
                .areAnnotatedWith(Configuration.class)
                .should().haveSimpleNameEndingWith("Configuration")
                .check(config.getClasses()))
    );
  }
}
