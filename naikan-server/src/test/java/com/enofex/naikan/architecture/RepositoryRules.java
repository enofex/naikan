package com.enofex.naikan.architecture;

import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameMatching;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.enofex.naikan.test.architecture.ArchUnitTestsConfig;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.springframework.stereotype.Repository;

final class RepositoryRules {

  private static final String REPOSITORY = ".*Repository";

  private RepositoryRules() {
  }

  static List<DynamicTest> repositoryRules(ArchUnitTestsConfig config) {
    return List.of(
        dynamicTest("Service implementations should be named *Repository",
            () -> classes()
                .that().implement(nameMatching(REPOSITORY))
                .should().haveNameMatching(".*Repository")
                .check(config.getClasses())),

        dynamicTest("Repository implementations should be annotated with @Repository",
            () -> classes()
                .that().implement(nameMatching(REPOSITORY))
                .should().beAnnotatedWith(Repository.class)
                .check(config.getClasses()))
    );
  }
}
