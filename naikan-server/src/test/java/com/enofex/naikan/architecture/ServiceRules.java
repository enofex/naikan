package com.enofex.naikan.architecture;

import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;
import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameMatching;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.enofex.naikan.test.architecture.ArchUnitTestsConfig;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

final class ServiceRules {

  private static final String SERVICE = ".*Service";

  private ServiceRules() {
  }

  static List<DynamicTest> serviceRules(ArchUnitTestsConfig config) {
    return List.of(
        dynamicTest("Service implementations should be named *Service",
            () -> classes()
                .that().implement(nameMatching(SERVICE))
                .should().haveNameMatching(".*Service")
                .check(config.getClasses())),

        dynamicTest("Service implementations should be annotated with @Service",
            () -> classes()
                .that().implement(nameMatching(SERVICE))
                .should().beAnnotatedWith(Service.class)
                .check(config.getClasses())),

        dynamicTest("Service implementations should be annotated with @Transactional",
            () -> classes()
                .that(annotatedWith(Service.class))
                .should().beAnnotatedWith(Transactional.class)
                .check(config.getClasses())),

        dynamicTest("Service implementations should have only final fields",
            () -> classes()
                .that().implement(nameMatching(SERVICE))
                .should().haveOnlyFinalFields()
                .check(config.getClasses())));
  }
}
