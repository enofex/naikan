package com.enofex.naikan.architecture;

import com.enofex.taikai.Taikai;
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
                .classesShouldNotBeAnnotatedWithDisabled()
                .classesShouldBePackagePrivate(".*Test")
                .classesShouldBePackagePrivate(".*IT")
                .methodsShouldNotBeAnnotatedWithDisabled()
                .methodsShouldMatch("should.*")
                .methodsShouldBePackagePrivate()))
        .java(java -> java
            .imports(imports -> imports
                .shouldHaveNoCycles()
                .shouldNotImport("..shaded..")
                .shouldNotImport("..internal..")
                .shouldNotImport("..lombok..")
                .shouldNotImport("org.junit.."))
            .naming(naming -> naming
                .constantsShouldFollowConventions()
                .classesShouldNotMatch(".*Impl")
                .interfacesShouldNotHavePrefixI()))
        .build();

    taikai.check();
  }
}
