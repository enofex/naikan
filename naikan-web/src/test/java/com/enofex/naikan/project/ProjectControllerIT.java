package com.enofex.naikan.project;

import static com.enofex.naikan.test.model.Boms.validBom0asInputStream;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.deserializer.DeserializerFactory;
import com.enofex.naikan.test.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
class ProjectControllerIT {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private MongoTemplate template;

  @Test
  void shouldFindAll() throws Exception {
    Bom savedBom = this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects"))
        .andExpect(handler().methodName("findAll"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].id").value(savedBom.id()));
  }

  @Test
  void shouldFindBomDetailById() throws Exception {
    Bom savedBom = this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/" + savedBom.id()))
        .andExpect(handler().methodName("findBomDetailById"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(savedBom.id()));
  }

  @Test
  void shouldNotFindBomDetailById() throws Exception {
    this.mvc.perform(
            get("/api/projects/1234"))
        .andExpect(handler().methodName("findBomDetailById"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldFindDeploymentsById() throws Exception {
    Bom savedBom = this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/%s/deployments".formatted(savedBom.id())))
        .andExpect(handler().methodName("findDeploymentsById"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldFindDeploymentsPerMonthById() throws Exception {
    Bom savedBom = this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/%s/deployments/months".formatted(savedBom.id())))
        .andExpect(handler().methodName("findDeploymentsPerMonthById"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldFindDeploymentsPerMonth() throws Exception {
    this.template.save(DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/deployments/months".formatted()))
        .andExpect(handler().methodName("findDeploymentsPerMonth"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldFindDeploymentsPerProject() throws Exception {
    this.template.save(DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/deployments/projects".formatted()))
        .andExpect(handler().methodName("findDeploymentsPerProject"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldFindGroupedDeploymentsPerVersionById() throws Exception {
    Bom savedBom = this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/%s/versions/grouped".formatted(savedBom.id())))
        .andExpect(handler().methodName("findGroupedDeploymentsPerVersionById"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldFindLatestVersionPerEnvironmentById() throws Exception {
    Bom savedBom = this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/%s/versions/environments".formatted(savedBom.id())))
        .andExpect(handler().methodName("findLatestVersionPerEnvironmentById"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldFindFilter() throws Exception {
    this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/filter"))
        .andExpect(handler().methodName("findFilter"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(22));
  }

  @Test
  void shouldExportAllToXlsx() throws Exception {
    this.mvc.perform(
            get("/api/projects?xlsx")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
        .andExpect(handler().methodName("xlsxAll"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldExportToXlsxById() throws Exception {
    Bom savedBom = this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/%s?xlsx".formatted(savedBom.id()))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
        .andExpect(handler().methodName("xlsxById"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldFindCommitsById() throws Exception {
    Bom savedBom = this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/%s/commits".formatted(savedBom.id())))
        .andExpect(handler().methodName("findCommitsById"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldFindCommitsPerMonthById() throws Exception {
    Bom savedBom = this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/%s/commits/months".formatted(savedBom.id())))
        .andExpect(handler().methodName("findCommitsPerMonthById"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldFindCommitsPerMonth() throws Exception {
    this.template.save(DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/commits/months"))
        .andExpect(handler().methodName("findCommitsPerMonth"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }


  @Test
  void shouldFindCommitsPerProject() throws Exception {
    this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/commits/projects"))
        .andExpect(handler().methodName("findCommitsPerProject"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }


  @Test
  void shouldFindRepositoryTagsById() throws Exception {
    Bom savedBom = this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/%s/repository/tags".formatted(savedBom.id())))
        .andExpect(handler().methodName("findRepositoryTagsById"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  void shouldFindRepositoryBranchesById() throws Exception {
    Bom savedBom = this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            get("/api/projects/%s/repository/branches".formatted(savedBom.id())))
        .andExpect(handler().methodName("findRepositoryBranchesById"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON));
  }
}