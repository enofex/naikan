package com.enofex.naikan.restapi.deployment;

import static com.enofex.naikan.test.Tokens.token;
import static com.enofex.naikan.test.model.Boms.validBom0asInputStream;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.model.deserializer.DeserializerFactory;
import com.enofex.naikan.test.IntegrationTest;
import com.enofex.naikan.test.model.Boms;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
class ApiDeploymentsControllerIT {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private ObjectMapper mapper;
  @Autowired
  private MongoTemplate template;

  @BeforeEach
  void beforeEach() {
    this.template.save(token(), "tokens");
  }

  @Test
  void shouldSaveBomById() throws Exception {
    Bom savedBom = this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            post("/api/public/projects/" + savedBom.id() + "/deployments")
                .header("authorization", "Bearer " + token().token())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(deployment())))
        .andExpect(handler().methodName("saveById"))
        .andExpect(status().isCreated())
        .andExpect(
            redirectedUrl(
                "http://localhost/api/public/projects/" + savedBom.id() + "/deployments/2"));
  }

  @Test
  void shouldNotSaveBomById() throws Exception {
    this.mvc.perform(
            post("/api/public/projects/1/deployments")
                .header("authorization", "Bearer " + token().token())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Boms.invalidBom0asInputStream().readAllBytes()))
        .andExpect(handler().methodName("saveById"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldNotSaveBomWhenIsUnauthorized() throws Exception {
    this.mvc.perform(
            post("/api/public/projects/1/deployments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Boms.invalidBom0asInputStream().readAllBytes()))
        .andExpect(status().isUnauthorized());
  }

  private static Deployment deployment() {
    return new Deployment(
        "env",
        "location",
        "1.0",
        LocalDateTime.now());
  }
}