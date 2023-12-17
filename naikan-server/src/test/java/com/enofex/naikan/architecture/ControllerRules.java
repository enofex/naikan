package com.enofex.naikan.architecture;

import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameMatching;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.all;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.enofex.naikan.test.architecture.ArchUnitTestsConfig;
import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMember;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.properties.CanBeAnnotated;
import com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates;
import com.tngtech.archunit.core.domain.properties.HasOwner.Functions.Get;
import com.tngtech.archunit.lang.AbstractClassesTransformer;
import com.tngtech.archunit.lang.ClassesTransformer;
import com.tngtech.archunit.lang.conditions.ArchConditions;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

final class ControllerRules {

  private static final String CONTROLLER = ".*Controller";

  private ControllerRules() {
  }

  static List<DynamicTest> controllerRules(ArchUnitTestsConfig config) {
    return List.of(
        dynamicTest("Controllers should be annotated with @RestController",
            () -> classes()
                .that().haveNameMatching(CONTROLLER)
                .should().beAnnotatedWith(RestController.class)
                .check(config.getClasses())),

        dynamicTest("Controllers should be package private",
            () -> classes()
                .that().areAnnotatedWith(RestController.class)
                .should().bePackagePrivate()
                .check(config.getClasses())),

        dynamicTest("All controller request mapping methods should be public",
            () -> all(methods())
                .that(areDefinedInAController())
                .and(mappingMethods())
                .should(ArchConditions.bePublic())
                .check(config.getClasses())),

        dynamicTest(
            "Controller classes should not depend on each other",
            () -> classes()
                .that().areAnnotatedWith(RestController.class)
                .should().dependOnClassesThat().haveNameMatching(CONTROLLER)
                .check(config.getClasses()))
    );
  }

  private static ClassesTransformer<JavaMethod> methods() {

    return new AbstractClassesTransformer<>("methods") {
      @Override
      public Iterable<JavaMethod> doTransform(JavaClasses javaClasses) {
        List<JavaMethod> methods = new ArrayList<>();
        for (JavaClass javaClass : javaClasses) {
          methods.addAll(javaClass.getMethods());
        }
        return methods;
      }
    };
  }

  private static DescribedPredicate<? super JavaMember> areDefinedInAController() {
    return Get.<JavaClass>owner().is(nameMatching(CONTROLLER));
  }

  private static DescribedPredicate<CanBeAnnotated> mappingMethods() {
    return Predicates.annotatedWith(GetMapping.class)
        .or(Predicates.annotatedWith(PostMapping.class)
            .or(Predicates.annotatedWith(PutMapping.class))
            .or(Predicates.annotatedWith(RequestMapping.class))
            .or(Predicates.annotatedWith(DeleteMapping.class)));
  }
}
