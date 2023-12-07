package com.enofex.naikan.overview.contact;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.model.deserializer.DeserializerFactory;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.test.IntegrationTest;
import com.enofex.naikan.test.model.Boms;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

@IntegrationTest
class OverviewContactRepositoryIT {

  @Autowired
  private MongoTemplate template;

  private OverviewContactRepository repository;

  @BeforeEach
  void setUp() {
    this.repository = new OverviewContactRepository(this.template);
    this.template.save(DeserializerFactory.newJsonDeserializer().of(Boms.validBom0asInputStream()),
        "projects");
  }

  @Test
  void shouldFindAll() {
    Page<OverviewGroup> page = this.repository.findAll(
        Filterable.emptySearch(),
        Pageable.ofSize(20));

    assertEquals(1, page.getContent().size());
  }

  @Test
  void shouldFindAllOverviewsWithSearch() {
    Page<OverviewGroup> page = this.repository.findAll(
        Filterable.of("John"), Pageable.ofSize(20));

    assertEquals(1, page.getContent().size());
  }
}