package com.enofex.naikan.administration.project.support;

import static com.enofex.naikan.model.deserializer.DeserializerFactory.newJsonDeserializer;
import static com.enofex.naikan.test.model.Boms.validBom0asInputStream;
import static com.enofex.naikan.test.model.Boms.validBom1asInputStream;
import static com.enofex.naikan.test.model.Boms.validBom2asInputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.ProjectId;
import com.enofex.naikan.administration.project.AdministrationProjectRepository;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.test.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;

@IntegrationTest
class AdministrationApiProjectMongoRepositoryIT {

  @Autowired
  private MongoTemplate template;

  private AdministrationProjectRepository repository;

  @BeforeEach
  void setUp() {
    this.repository = new AdministrationProjectMongoRepository(this.template);
  }

  @Test
  void shouldFindAll() {
    this.template.save(newJsonDeserializer().of(validBom0asInputStream()), "projects");
    this.template.save(newJsonDeserializer().of(validBom1asInputStream()), "projects");
    this.template.save(newJsonDeserializer().of(validBom2asInputStream()), "projects");

    assertEquals(3, this.repository.findAll(Filterable.emptySearch(),
        Pageable.ofSize(10)).getTotalElements());

    assertEquals(0, this.repository.findAll(Filterable.of("do_not_exists"),
        Pageable.ofSize(10)).getTotalElements());

    assertEquals(1, this.repository.findAll(Filterable.of("Naikan III"),
        Pageable.ofSize(10)).getTotalElements());
  }

  @Test
  void shouldDelete() {
    Bom savedBom = this.template.save(newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    assertEquals(1, this.repository.findAll(Filterable.emptySearch(),
        Pageable.ofSize(10)).getTotalElements());

    this.repository.delete(ProjectId.of(savedBom.id()));

    assertEquals(0, this.repository.findAll(Filterable.emptySearch(),
        Pageable.ofSize(10)).getTotalElements());
  }
}