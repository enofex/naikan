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
        dynamicTest("Repository implementations should be named *MongoRepository",
            () -> classes()
                .that().implement(nameMatching(REPOSITORY))
                .should().haveNameMatching(".*MongoRepository")
                .check(config.getClasses())),

        dynamicTest("Repository implementations should reside in support package",
            () -> classes()
                .that().implement(nameMatching(REPOSITORY))
                .should().resideInAPackage("..support..")
                .check(config.getClasses())),

        dynamicTest("Repository implementations should be annotated with @Repository",
            () -> classes()
                .that().implement(nameMatching(REPOSITORY))
                .should().beAnnotatedWith(Repository.class)
                .check(config.getClasses())),

        dynamicTest(
            "Repositories should only be accessed by *Repository or *ServiceHandler classes",
            () -> classes()
                .that().haveNameMatching(REPOSITORY)
                .should().onlyBeAccessed()
                .byClassesThat(nameMatching(REPOSITORY).or(nameMatching(".*ServiceHandler.*")))
                .check(config.getClasses()))
    );
  }
}
