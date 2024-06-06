package com.enofex.naikan.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.enofex.naikan.test.architecture.ArchUnitTestsConfig;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.util.List;
import org.junit.jupiter.api.DynamicTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

final class SpringRules {

  private SpringRules() {
  }

  static List<DynamicTest> springBootRules(ArchUnitTestsConfig config) {
    return List.of(
        dynamicTest(
            "Classes annotated with @SpringBootApplication should be located in the default application package",
            () -> classes()
                .that().areAnnotatedWith(SpringBootApplication.class)
                .should().resideInAPackage("com.enofex.naikan")
                .check(config.getClasses())),

        dynamicTest("Constructor dependency should be used",
            () -> noFields()
                .should().beAnnotatedWith(Autowired.class)
                .check(config.getClasses())),

        dynamicTest("Spring singleton components should only have final fields",
            () -> classes()
                .that().areAnnotatedWith(Service.class)
                .or().areAnnotatedWith(Component.class)
                .and().areNotAnnotatedWith(ConfigurationProperties.class)
                .or().areAnnotatedWith(Controller.class)
                .or().areAnnotatedWith(RestController.class)
                .or().areAnnotatedWith(Repository.class)
                .should().haveOnlyFinalFields().check(config.getClasses()))
    );
  }

  private static ArchCondition<JavaField> notBeAutowired() {
    return new ArchCondition<>("not be autowired") {

      @Override
      public void check(JavaField javaField, ConditionEvents events) {
        if (javaField.isAnnotatedWith(Autowired.class)) {
          events.add(SimpleConditionEvent.violated(javaField,
              javaField.getOwner().getSimpleName() + "." + javaField.getName()));
        }
      }
    };
  }
}
