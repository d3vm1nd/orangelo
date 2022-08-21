package com.d3vm1nd.orangelo;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@ExtendWith(MockitoExtension.class)
class ArchitectureUTest {

  private static JavaClasses importedClasses;

  @BeforeAll
  static void settings() {
    importedClasses =
        new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_JARS)
            .importPackages("com.d3vm1nd");
  }

  @Test
  void some_architecture_rule() {
    ArchRule rule = layeredArchitecture().consideringAllDependencies().layer("infra")
        .definedBy("com.d3vm1nd.chinotto.infra..").layer("app")
        .definedBy("com.d3vm1nd.chinotto.app..").layer("domain")
        .definedBy("com.d3vm1nd.chinotto.domain..").whereLayer("infra").mayNotBeAccessedByAnyLayer()
        .whereLayer("app").mayOnlyBeAccessedByLayers("infra").whereLayer("domain")
        .mayOnlyBeAccessedByLayers("app", "infra").allowEmptyShould(true);
    rule.check(importedClasses);
  }

  @Test
  void component_rule() {
    ArchRule rule = classes().that().areAnnotatedWith(Component.class).or()
        .areAnnotatedWith(Repository.class).or().areAnnotatedWith(RestControllerEndpoint.class)
        .should().resideInAPackage("..infra..");
    rule.check(importedClasses);
  }

  @Test
  void service_rule() {
    ArchRule rule =
        classes().that().areAnnotatedWith(Service.class).should().resideInAPackage("..app..");
    rule.check(importedClasses);
  }

  @Test
  void app_rule() {
    ArchRule rule = classes().that().resideInAPackage("..app..").should().onlyDependOnClassesThat()
        .resideInAnyPackage("..app..", "..domain..", "java..", "lombok..", "org.slf4j..",
            "org.springframework.stereotype..");
    rule.check(importedClasses);
  }

  @Test
  void app_in_rule() {
    ArchRule rule = classes().that().resideInAPackage("..app.in..").should().beInterfaces();
    rule.check(importedClasses);
  }

  @Test
  void app_out_rule() {
    ArchRule rule = classes().that().resideInAPackage("..app.out..").should().beInterfaces();
    rule.check(importedClasses);
  }

  @Test
  void domain_rule() {
    ArchRule rule =
        classes().that().resideInAPackage("..domain..").should().onlyDependOnClassesThat()
            .resideInAnyPackage("..domain..", "java..", "lombok..", "org.slf4j..");
    rule.check(importedClasses);
  }

}
