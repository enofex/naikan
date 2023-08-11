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
    Bom savedBom = template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    mvc.perform(
            get("/api/projects"))
        .andExpect(handler().methodName("findAll"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].id").value(savedBom.id()));
  }

  @Test
  void shouldFindById() throws Exception {
    Bom savedBom = template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    mvc.perform(
            get("/api/projects/" + savedBom.id()))
        .andExpect(handler().methodName("findById"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(savedBom.id()));
  }

  @Test
  void shouldNotFindById() throws Exception {
    mvc.perform(
            get("/api/projects/1234"))
        .andExpect(handler().methodName("findById"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldFindFilter() throws Exception {
    template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    mvc.perform(
            get("/api/projects/filter"))
        .andExpect(handler().methodName("findFilter"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.length()").value(22));
  }

  @Test
  void shouldExportAllToXlsx() throws Exception {
    mvc.perform(
            get("/api/projects?xlsx")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
        .andExpect(handler().methodName("xlsxAll"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldExportToXlsx() throws Exception {
    Bom savedBom = template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    mvc.perform(
            get("/api/projects/%s?xlsx".formatted(savedBom.id()))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
        .andExpect(handler().methodName("xlsx"))
        .andExpect(status().isOk());
  }
}