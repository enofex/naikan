package com.enofex.naikan.architecture;

import static com.tngtech.archunit.core.domain.JavaModifier.FINAL;
import static com.tngtech.archunit.core.domain.JavaModifier.STATIC;

import com.enofex.taikai.Taikai;
import java.util.List;
import org.apache.commons.logging.Log;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

  @Test
  void shouldFulfilConstrains() {
    Taikai taikai = Taikai.builder()
        .namespace("com.enofex.naikan")
        .spring(spring -> spring
            .noAutowiredFields()
            .boot(boot -> boot
                .springBootApplicationShouldBeIn("com.enofex.naikan"))
            .controllers(controllers -> controllers
                .shouldBeAnnotatedWithRestController()
                .namesShouldEndWithController()
                .shouldNotDependOnOtherControllers()
                .shouldBePackagePrivate())
            .services(services -> services
                .shouldBeAnnotatedWithService()
                .shouldNotDependOnControllers()
                .namesShouldEndWithService())
            .repositories(repositories -> repositories
                .shouldNotDependOnServices()
                .namesShouldEndWithRepository()))
        .test(test -> test
            .junit5(junit5 -> junit5
                .methodsShouldContainAssertionsOrVerifications()
                .classesShouldNotBeAnnotatedWithDisabled()
                .classesShouldBePackagePrivate(".*Test")
                .classesShouldBePackagePrivate(".*IT")
                .methodsShouldNotBeAnnotatedWithDisabled()
                .methodsShouldMatch("should.*")
                .methodsShouldBePackagePrivate()))
        .logging(logging -> logging
            .loggersShouldFollowConventions(Log.class, "logger"))
        .java(java -> java
            .utilityClassesShouldBeFinalAndHavePrivateConstructor()
            .classesShouldImplementHashCodeAndEquals()
            .noUsageOfSystemOutOrErr()
            .serialVersionUIDFieldsShouldBeStaticFinalLong()
            .fieldsShouldHaveModifiers("^[A-Z][A-Z0-9_]*$", List.of(STATIC, FINAL))
            .classesShouldResideInPackage("com.enofex.naikan..")
            .imports(imports -> imports
                .shouldHaveNoCycles()
                .shouldNotImport("..shaded..")
                .shouldNotImport("..internal..")
                .shouldNotImport("..lombok..")
                .shouldNotImport("org.junit.."))
            .naming(naming -> naming
                .packagesShouldMatchDefault()
                .constantsShouldFollowConventions()
                .classesShouldNotMatch(".*Impl")
                .interfacesShouldNotHavePrefixI()))
        .build();

    taikai.check();
  }
}
