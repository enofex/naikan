package com.enofex.naikan.administration.project;

import static com.enofex.naikan.test.model.Boms.validBom0asInputStream;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@WithMockUser(authorities = "ROLE_ADMIN")
class AdministrationProjectControllerIT {

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
            get("/api/administration/projects"))
        .andExpect(handler().methodName("findAll"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].id").value(savedBom.id()));
  }

  @Test
  void shouldDelete() throws Exception {
    Bom savedBom = template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    mvc.perform(
            delete("/api/administration/projects/" + savedBom.id())
                .with(csrf()))
        .andExpect(handler().methodName("delete"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldNotDelete() throws Exception {
    mvc.perform(
            delete("/api/administration/projects/not-exist")
                .with(csrf()))
        .andExpect(handler().methodName("delete"))
        .andExpect(status().isNotFound());
  }
}