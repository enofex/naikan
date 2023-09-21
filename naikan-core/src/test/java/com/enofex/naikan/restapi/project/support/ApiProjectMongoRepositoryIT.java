package com.enofex.naikan.restapi.project.support;

import static com.enofex.naikan.model.deserializer.DeserializerFactory.newJsonDeserializer;
import static com.enofex.naikan.test.model.Boms.validBom0asInputStream;
import static com.enofex.naikan.test.model.Boms.validBom1asInputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.model.Deployments;
import com.enofex.naikan.restapi.project.ApiProjectRepository;
import com.enofex.naikan.test.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

@IntegrationTest
class ApiProjectMongoRepositoryIT {

  @Autowired
  private MongoTemplate template;

  private ApiProjectRepository repository;

  @BeforeEach
  void setUp() {
    this.repository = new ApiProjectMongoRepository(this.template);
  }

  @Test
  void shouldUpdateByProjectIdBom() {
    Bom bom = newJsonDeserializer().of(validBom1asInputStream());
    Bom savedBom = this.repository.upsertByProjectName(bom);

    this.repository.updateById(ProjectId.of(savedBom.id()),
        newJsonDeserializer().of(validBom0asInputStream()));

    assertEquals(1L, this.template.count(new Query(), "projects"));
  }

  @Test
  void shouldNotUpdateByProjectIdBom() {
    this.repository.updateById(ProjectId.of("do_not_exits"),
        newJsonDeserializer().of(validBom0asInputStream()));

    assertEquals(0L, this.template.count(new Query(), "projects"));
  }

  @Test
  void shouldUpdateBom() {
    this.repository.upsertByProjectName(newJsonDeserializer().of(validBom0asInputStream()));
    this.repository.upsertByProjectName(newJsonDeserializer().of(validBom0asInputStream()));

    assertEquals(1L, this.template.count(new Query(), "projects"));
  }

  @Test
  void shouldSaveBom() {
    this.repository.upsertByProjectName(newJsonDeserializer().of(validBom0asInputStream()));
    this.repository.upsertByProjectName(newJsonDeserializer().of(validBom1asInputStream()));

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

    Bom savedBom = this.repository.upsertByProjectName(modifiedBom);

    assertEquals(1, savedBom.deployments().all().size());
    assertEquals(1L, this.template.count(new Query(), "projects"));
  }

  @Test
  void shouldFindById() {
    Bom bom = newJsonDeserializer().of(validBom1asInputStream());
    Bom savedBom = this.repository.upsertByProjectName(bom);

    assertEquals(savedBom.id(), this.repository.findById(ProjectId.of(savedBom.id())).get().id());
  }

  @Test
  void shouldExistsByProjectName() {
    Bom bom = newJsonDeserializer().of(validBom1asInputStream());
    Bom savedBom = this.repository.upsertByProjectName(bom);

    assertTrue(this.repository.existsByProjectName(savedBom.project().name()));
  }

  @Test
  void shouldNotExistsByProjectName() {
    Bom bom = newJsonDeserializer().of(validBom1asInputStream());

    assertFalse(this.repository.existsByProjectName(bom.project().name()));
  }

}