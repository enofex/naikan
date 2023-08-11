package com.enofex.naikan.overview.environment.support;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.model.deserializer.DeserializerFactory;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.OverviewTopGroups;
import com.enofex.naikan.overview.environment.OverviewEnvironmentRepository;
import com.enofex.naikan.test.IntegrationTest;
import com.enofex.naikan.test.model.Boms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

@IntegrationTest
class OverviewEnvironmentMongoRepositoryIT {

  @Autowired
  private MongoTemplate template;

  private OverviewEnvironmentRepository repository;

  @BeforeEach
  void setUp() {
    this.repository = new OverviewEnvironmentMongoRepository(this.template);
    this.template.save(DeserializerFactory.newJsonDeserializer().of(Boms.validBom0asInputStream()),
        "projects");
  }

  @Test
  void shouldFindAll() {
    Page<OverviewGroup> page = this.repository.findAll(
        Filterable.emptySearch(), Pageable.ofSize(20));

    assertEquals(2, page.getContent().size());
  }

  @Test
  void shouldFindAllOverviewsWithSearch() {
    Page<OverviewGroup> page = this.repository.findAll(
        Filterable.of("Staging"), Pageable.ofSize(20));

    assertEquals(1, page.getContent().size());
  }


  @Test
  void shouldFindTopEnvironments() {
    OverviewTopGroups groups = this.repository.findTopEnvironments(5);

    assertEquals(2, groups.counts().size());
    assertEquals(2, groups.names().size());
  }
}