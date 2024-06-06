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
                .namesShouldEndWithService())
            .repositories(repositories -> repositories
                .namesShouldEndWithRepository()))
        .test(test -> test
            .junit5(junit5 -> junit5
                .classesShouldNotBeAnnotatedWithDisabled()
                .methodsShouldNotBeAnnotatedWithDisabled()))
        .java(java -> java
            .imports(imports -> imports
                .shouldHaveNoCycles()
                .shouldNotImport("..shaded..")
                .shouldNotImport("..lombok..")
                .shouldNotImport("org.junit.."))
            .naming(naming -> naming
                .classesShouldNotMatch(".*Impl")
                .interfacesShouldNotHavePrefixI()))
        .build();

    taikai.check();
  }
}
