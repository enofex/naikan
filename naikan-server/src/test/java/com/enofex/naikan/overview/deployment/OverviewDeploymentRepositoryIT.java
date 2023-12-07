package com.enofex.naikan.overview.deployment;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.model.deserializer.DeserializerFactory;
import com.enofex.naikan.overview.OverviewTopGroups;
import com.enofex.naikan.test.IntegrationTest;
import com.enofex.naikan.test.model.Boms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

@IntegrationTest
class OverviewDeploymentRepositoryIT {

  @Autowired
  private MongoTemplate template;

  private OverviewDeploymentRepository repository;

  @BeforeEach
  void setUp() {
    this.repository = new OverviewDeploymentRepository(this.template);
    this.template.save(DeserializerFactory.newJsonDeserializer().of(Boms.validBom0asInputStream()),
        "projects");
  }

  @Test
  void shouldFindAll() {
    Page<OverviewDeployment> page = this.repository.findAll(
        Filterable.emptySearch(), Pageable.ofSize(20));

    assertEquals(2, page.getContent().size());
  }

  @Test
  void shouldFindAllOverviewsWithSearch() {
    Page<OverviewDeployment> page = this.repository.findAll(
        Filterable.of("Staging"), Pageable.ofSize(20));

    assertEquals(1, page.getContent().size());
  }

  @Test
  void shouldFindTopProjects() {
    OverviewTopGroups groups = this.repository.findTopProjects(5);

    assertEquals(1, groups.counts().size());
    assertEquals(1, groups.names().size());
  }
}