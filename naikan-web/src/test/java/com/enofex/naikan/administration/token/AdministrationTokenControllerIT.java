package com.enofex.naikan.administration.token;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.enofex.naikan.test.IntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@WithMockUser(authorities = "ROLE_ADMIN")
class AdministrationTokenControllerIT {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private MongoTemplate template;
  @Autowired
  private ObjectMapper mapper;

  @Test
  void shouldFindAll() throws Exception {
    this.template.save(token(), "tokens");

    this.mvc.perform(
            get("/api/administration/tokens"))
        .andExpect(handler().methodName("findAll"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].token").value(
            token().token()));
  }

  @Test
  void shouldDeleteById() throws Exception {
    Token savedToken = this.template.save(token(), "tokens");

    this.mvc.perform(
            delete("/api/administration/tokens/" + savedToken.id())
                .with(csrf()))
        .andExpect(handler().methodName("deleteById"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldNotDeleteById() throws Exception {
    this.mvc.perform(
            delete("/api/administration/tokens/not-exist")
                .with(csrf()))
        .andExpect(handler().methodName("deleteById"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldSave() throws Exception {
    this.mvc.perform(
            post("/api/administration/tokens")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(token())))
        .andExpect(handler().methodName("save"))
        .andExpect(status().isCreated())
        .andExpect(redirectedUrlPattern("http://localhost/api/administration/tokens/*"));
  }

  private static Token token() {
    return new Token("token",
        "createdBy",
        LocalDateTime.now(),
        LocalDateTime.now(),
        "description");
  }
}