package com.enofex.naikan.administration.user;

import static com.enofex.naikan.test.Users.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.enofex.naikan.security.AuthorityType;
import com.enofex.naikan.security.User;
import com.enofex.naikan.test.IntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@WithMockUser(authorities = "ROLE_ADMIN")
class AdministrationUserControllerIT {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private MongoTemplate template;
  @Autowired
  private ObjectMapper mapper;

  @Test
  void shouldFindAll() throws Exception {
    this.template.save(user(), "users");

    this.mvc.perform(
            get("/api/administration/users"))
        .andExpect(handler().methodName("findAll"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.content.length()").value(1))
        .andExpect(jsonPath("$.content[0].name").value(
            user().name()));
  }

  @Test
  void shouldUpdateAuthoritiesById() throws Exception {
    User saveUser = this.template.save(user(), "users");
    String authorities = this.mapper.writeValueAsString(
        new String[]{AuthorityType.ROLE_ADMIN.getAuthority()});

    this.mvc.perform(
            patch("/api/administration/users/" + saveUser.id())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorities))
        .andExpect(handler().methodName("updateAuthoritiesById"))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldNotUpdateAuthoritiesById() throws Exception {
    String authorities = this.mapper.writeValueAsString(
        new String[]{AuthorityType.ROLE_ADMIN.getAuthority()});

    this.mvc.perform(
            patch("/api/administration/users/not-exist")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(authorities))
        .andExpect(handler().methodName("updateAuthoritiesById"))
        .andExpect(status().isNotFound());
  }

  @Test
  void shouldDeleteById() throws Exception {
    User savedUser = this.template.save(user(), "users");

    this.mvc.perform(
            delete("/api/administration/users/" + savedUser.id())
                .with(csrf()))
        .andExpect(handler().methodName("deleteById"))
        .andExpect(status().isOk());
  }

  @Test
  void shouldNotDeleteById() throws Exception {
    this.mvc.perform(
            delete("/api/administration/users/not-exist")
                .with(csrf()))
        .andExpect(handler().methodName("deleteById"))
        .andExpect(status().isNotFound());
  }
}