package com.enofex.naikan.project;

import static com.enofex.naikan.test.model.Boms.validBom0asInputStream;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.enofex.naikan.administration.token.Token;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.deserializer.DeserializerFactory;
import com.enofex.naikan.test.IntegrationTest;
import com.enofex.naikan.test.model.Boms;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
class ApiProjectControllerIT {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private MongoTemplate template;

  private final Token token = token();

  @BeforeEach
  void beforeEach() {
    this.template.save(token(), "tokens");
  }

  @Test
  void shouldSaveBom() throws Exception {
    this.mvc.perform(
            post("/api/public/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + this.token.token())
                .content(validBom0asInputStream().readAllBytes()))
        .andExpect(handler().methodName("upsertByProjectName"))
        .andExpect(status().isCreated())
        .andExpect(redirectedUrlPattern("http://localhost/api/projects/*"));
  }

  @Test
  void shouldUpdateBom() throws Exception {
    this.mvc.perform(
            post("/api/public/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + this.token.token())
                .content(validBom0asInputStream().readAllBytes()))
        .andExpect(handler().methodName("upsertByProjectName"))
        .andExpect(status().isCreated())
        .andExpect(redirectedUrlPattern("http://localhost/api/projects/*"));

    this.mvc.perform(
            post("/api/public/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + this.token.token())
                .content(validBom0asInputStream().readAllBytes()))
        .andExpect(handler().methodName("upsertByProjectName"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldNotSaveBom() throws Exception {
    this.mvc.perform(
            post("/api/public/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + this.token.token())
                .content(Boms.invalidBom3asInputStream().readAllBytes()))
        .andExpect(handler().methodName("upsertByProjectName"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldNotSaveBomWhenIsUnauthorized() throws Exception {
    this.mvc.perform(
            post("/api/public/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Boms.validBom0asInputStream().readAllBytes()))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void shouldUpdateBomByProjectId() throws Exception {
    Bom savedBom = this.template.save(
        DeserializerFactory.newJsonDeserializer().of(validBom0asInputStream()),
        "projects");

    this.mvc.perform(
            put("/api/public/projects/%s".formatted(savedBom.id()))
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + this.token.token())
                .content(validBom0asInputStream().readAllBytes()))
        .andExpect(handler().methodName("update"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldNotUpdateBomByProjectId() throws Exception {
    this.mvc.perform(
            put("/api/public/projects/%s".formatted("do_not_exits"))
                .contentType(MediaType.APPLICATION_JSON)
                .header("authorization", "Bearer " + this.token.token())
                .content(validBom0asInputStream().readAllBytes()))
        .andExpect(handler().methodName("update"))
        .andExpect(status().isNotFound());
  }

  private static Token token() {
    return new Token("token",
        "createdBy",
        LocalDateTime.now(),
        LocalDateTime.now(),
        "description");
  }
}