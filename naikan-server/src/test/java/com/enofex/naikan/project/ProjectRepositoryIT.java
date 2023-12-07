package com.enofex.naikan.project;

import static com.enofex.naikan.model.deserializer.DeserializerFactory.newJsonDeserializer;
import static com.enofex.naikan.test.model.Boms.validBom0asInputStream;
import static com.enofex.naikan.test.model.Boms.validBom1asInputStream;
import static com.enofex.naikan.test.model.Boms.validBom2asInputStream;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.model.Deployments;
import com.enofex.naikan.test.IntegrationTest;
import com.enofex.naikan.web.ProjectId;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

@IntegrationTest
class ProjectRepositoryIT {

  @Autowired
  private MongoTemplate template;

  private ProjectRepository repository;

  @BeforeEach
  void setUp() {
    this.repository = new ProjectRepository(this.template);
  }

  @Test
  void shouldFindAll() {
    this.template.save(newJsonDeserializer().of(validBom0asInputStream()), "projects");
    this.template.save(newJsonDeserializer().of(validBom1asInputStream()), "projects");
    this.template.save(newJsonDeserializer().of(validBom2asInputStream()), "projects");

    assertEquals(3L, this.repository.findAll(Filterable.emptySearch(),
        Pageable.ofSize(10)).getTotalElements());

    assertEquals(0L, this.repository.findAll(Filterable.of("do_not_exists"),
        Pageable.ofSize(10)).getTotalElements());

    assertEquals(1L, this.repository.findAll(Filterable.of("Naikan III"),
        Pageable.ofSize(10)).getTotalElements());
  }

  @Test
  void shouldSaveBom() {
    this.template.save(newJsonDeserializer().of(validBom0asInputStream()), "projects");
    this.template.save(newJsonDeserializer().of(validBom1asInputStream()), "projects");

    assertEquals(2L, this.template.count(new Query(), "projects"));
  }

  @Test
  void shouldUpdateDeployments() {
    Bom bom = newJsonDeserializer().of(validBom1asInputStream());
    assertEquals(2, bom.deployments().all().size());

    Bom modifiedBom = bom.toBuilder()
        .deployments(new Deployments(List.of(new Deployment(
            "stage",
            "stage.host",
            "2",
            LocalDateTime.now()))))
        .build();

    Bom savedBom = this.template.save(modifiedBom, "projects");

    assertEquals(1, savedBom.deployments().all().size());
    assertEquals(1L, this.template.count(new Query(), "projects"));
  }

  @Test
  void shouldFindById() {
    Bom bom = newJsonDeserializer().of(validBom1asInputStream());
    Bom savedBom = this.template.save(bom, "projects");

    assertEquals(savedBom.id(), this.repository.findById(ProjectId.of(savedBom.id())).get().id());
  }

  @Test
  void shouldFindBomDetailById() {
    Bom bom = newJsonDeserializer().of(validBom1asInputStream());
    Bom savedBom = this.template.save(bom, "projects");

    assertEquals(savedBom.id(),
        this.repository.findBomDetailById(ProjectId.of(savedBom.id())).get().id());
  }

  @Test
  void shouldFindDeploymentsById() {
    Bom bom = newJsonDeserializer().of(validBom1asInputStream());
    Bom savedBom = this.template.save(bom, "projects");

    List<Deployment> deployments = this.repository.findDeploymentsById(
        ProjectId.of(savedBom.id()),
        Filterable.emptySearch(),
        Pageable.ofSize(10)).getContent();

    assertAll(
        () -> assertEquals(2, deployments.size()),
        () -> assertEquals("2.0.0", deployments.getFirst().version()),
        () -> assertEquals("staging.naikan.io", deployments.getFirst().location()),
        () -> assertEquals("Staging", deployments.getFirst().environment())
    );
  }


  @Test
  void shouldFindGroupedDeploymentsPerVersion() {
    Bom bom = newJsonDeserializer().of(validBom1asInputStream());
    Bom savedBom = this.template.save(bom, "projects");

    List<GroupedDeploymentsPerVersion> deployments = this.repository.findGroupedDeploymentsPerVersionById(
        ProjectId.of(savedBom.id()),
        Filterable.emptySearch(),
        Pageable.ofSize(10)).getContent();

    assertAll(
        () -> assertEquals(2, deployments.size()),
        () -> assertEquals("2.0.1", deployments.getFirst().version()),
        () -> assertEquals(1, deployments.getFirst().count()),
        () -> assertEquals(1, deployments.getFirst().deployments().size()),
        () -> assertEquals("2.0.1", deployments.getFirst().deployments().getFirst().version()),
        () -> assertEquals("naikan.io", deployments.getFirst().deployments().getFirst().location()),
        () -> assertEquals("Production", deployments.getFirst().deployments().getFirst().environment())
    );
  }

  @Test
  void shouldFindLatestVersionPerEnvironment() {
    Bom bom = newJsonDeserializer().of(validBom1asInputStream());
    Bom savedBom = this.template.save(bom, "projects");

    List<LatestVersionPerEnvironment> versions = this.repository.findLatestVersionPerEnvironmentById(
        ProjectId.of(savedBom.id()));

    assertAll(
        () -> assertEquals(2, versions.size()),
        () -> assertEquals("Production", versions.getFirst().environment()),
        () -> assertEquals("2.0.1", versions.getFirst().deployment().version()),
        () -> assertEquals("naikan.io", versions.getFirst().deployment().location()),
        () -> assertEquals("Production", versions.getFirst().deployment().environment())
    );
  }

  @Test
  void shouldFindDeploymentsPerMonth() {
    Bom bom = newJsonDeserializer().of(validBom1asInputStream());
    Bom savedBom = this.template.save(bom, "projects");

    CountsPerItems deployments = this.repository.findDeploymentsPerMonthById(
        ProjectId.of(savedBom.id()));

    assertAll(
        () -> assertEquals(13, deployments.names().size()),
        () -> assertEquals(13, deployments.counts().size()),
        () -> assertEquals(
            List.of("2022-12", "2023-01", "2023-02", "2023-03", "2023-04", "2023-05", "2023-06",
                "2023-07", "2023-08", "2023-09", "2023-10", "2023-11", "2023-12"), deployments.names()),
        () -> assertEquals(
            List.of(1L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 1L), deployments.counts())
    );
  }

  @Test
  void shouldFindFilterCriteria() {
    this.template.save(newJsonDeserializer().of(validBom0asInputStream()), "projects");
    this.template.save(newJsonDeserializer().of(validBom1asInputStream()), "projects");
    this.template.save(newJsonDeserializer().of(validBom2asInputStream()), "projects");

    ProjectFilter filter = this.repository.findFilter();

    assertAll(
        () -> assertEquals(1, filter.contacts().size()),
        () -> assertEquals(1, filter.contactRoles().size()),
        () -> assertEquals(2, filter.deployments().size()),
        () -> assertEquals(1, filter.developerDepartments().size()),
        () -> assertEquals(1, filter.developerOrganizations().size()),
        () -> assertEquals(2, filter.developerRoles().size()),
        () -> assertEquals(2, filter.environmentLocations().size()),
        () -> assertEquals(2, filter.environments().size()),
        () -> assertEquals(1, filter.environmentTags().size()),
        () -> assertEquals(2, filter.groupIds().size()),
        () -> assertEquals(5, filter.integrations().size()),
        () -> assertEquals(2, filter.integrationTags().size()),
        () -> assertEquals(1, filter.licenses().size()),
        () -> assertEquals(2, filter.organizationDepartments().size()),
        () -> assertEquals(2, filter.organizations().size()),
        () -> assertEquals(2, filter.packaging().size()),
        () -> assertEquals(2, filter.tags().size()),
        () -> assertEquals(1, filter.teams().size()),
        () -> assertEquals(2, filter.technologies().size()),
        () -> assertEquals(2, filter.technologyTags().size()),
        () -> assertEquals(2, filter.technologyVersions().size())
    );
  }
}