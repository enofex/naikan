package com.enofex.naikan.restapi.project.deployment.support;

import static com.enofex.naikan.test.model.Boms.validBom0asInputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.model.deserializer.DeserializerFactory;
import com.enofex.naikan.restapi.project.deployment.ApiDeploymentRepository;
import com.enofex.naikan.test.IntegrationTest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

@IntegrationTest
class ApiDeploymentsMongoRepositoryIT {

  @Autowired
  private MongoTemplate template;

  private ApiDeploymentRepository repository;

  @BeforeEach
  void setUp() {
    this.repository = new ApiDeploymentMongoRepository(this.template);
  }

  @Test
  void shouldAddDeploymentToBom() {
    Bom bom = DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream());

    assertEquals(2, bom.deployments().all().size());

    Bom savedBom = this.template.save(bom, "projects");
    Bom updatedDeployments = this.repository.save(ProjectId.of(savedBom.id()), deployment());

    assertEquals(3, updatedDeployments.deployments().all().size());
  }

  private static Deployment deployment() {
    return new Deployment(
        "production",
        "production.com",
        "1.1",
        LocalDateTime.now());
  }
}