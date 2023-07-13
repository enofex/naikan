package com.enofex.naikan.project.support;

import static com.enofex.naikan.model.deserializer.DeserializerFactory.newJsonDeserializer;
import static com.enofex.naikan.test.model.Boms.validBom0asInputStream;
import static com.enofex.naikan.test.model.Boms.validBom1asInputStream;
import static com.enofex.naikan.test.model.Boms.validBom2asInputStream;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.model.Deployments;
import com.enofex.naikan.project.ProjectFilter;
import com.enofex.naikan.project.ProjectRepository;
import com.enofex.naikan.test.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

@IntegrationTest
class ProjectMongoRepositoryIT {

  @Autowired
  private MongoTemplate template;

  private ProjectRepository repository;

  @BeforeEach
  void setUp() {
    this.repository = new ProjectMongoRepository(this.template);
  }

  @Test
  void shouldFindAll() {
    this.repository.upsertByProjectName(newJsonDeserializer().of(validBom0asInputStream()));
    this.repository.upsertByProjectName(newJsonDeserializer().of(validBom1asInputStream()));
    this.repository.upsertByProjectName(newJsonDeserializer().of(validBom2asInputStream()));

    assertEquals(3, this.repository.findAll(Filterable.emptySearch(),
        Pageable.ofSize(10)).getTotalElements());

    assertEquals(0, this.repository.findAll(Filterable.of("do_not_exists"),
        Pageable.ofSize(10)).getTotalElements());

    assertEquals(1, this.repository.findAll(Filterable.of("Naikan III"),
        Pageable.ofSize(10)).getTotalElements());
  }

  @Test
  void shouldUpdateByProjectIdBom() {
    Bom bom = newJsonDeserializer().of(validBom1asInputStream());
    Bom savedBom = this.repository.upsertByProjectName(bom);

    this.repository.update(ProjectId.of(savedBom.id()),
        newJsonDeserializer().of(validBom0asInputStream()));

    assertEquals(1, this.template.count(new Query(), "projects"));
  }

  @Test
  void shouldNotUpdateByProjectIdBom() {
    this.repository.update(ProjectId.of("do_not_exits"),
        newJsonDeserializer().of(validBom0asInputStream()));

    assertEquals(0, this.template.count(new Query(), "projects"));
  }

  @Test
  void shouldUpdateBom() {
    this.repository.upsertByProjectName(newJsonDeserializer().of(validBom0asInputStream()));
    this.repository.upsertByProjectName(newJsonDeserializer().of(validBom0asInputStream()));

    assertEquals(1, this.template.count(new Query(), "projects"));
  }

  @Test
  void shouldSaveBom() {
    this.repository.upsertByProjectName(newJsonDeserializer().of(validBom0asInputStream()));
    this.repository.upsertByProjectName(newJsonDeserializer().of(validBom1asInputStream()));

    assertEquals(2, this.template.count(new Query(), "projects"));
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

    Bom savedBom = this.repository.upsertByProjectName(modifiedBom);

    assertEquals(1, savedBom.deployments().all().size());
    assertEquals(1, this.template.count(new Query(), "projects"));
  }

  @Test
  void shouldFindById() {
    Bom bom = newJsonDeserializer().of(validBom1asInputStream());
    Bom savedBom = this.repository.upsertByProjectName(bom);

    assertEquals(savedBom.id(), this.repository.findById(ProjectId.of(savedBom.id())).get().id());
  }

  @Test
  void shouldFindFilterCriteria() {
    this.repository.upsertByProjectName(newJsonDeserializer().of(validBom0asInputStream()));
    this.repository.upsertByProjectName(newJsonDeserializer().of(validBom1asInputStream()));
    this.repository.upsertByProjectName(newJsonDeserializer().of(validBom2asInputStream()));

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