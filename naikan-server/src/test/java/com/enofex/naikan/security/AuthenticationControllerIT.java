package com.enofex.naikan.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.enofex.naikan.test.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@WithMockUser(username = "user@company.com", authorities = "ROLE_USER")
class AuthenticationControllerIT {

  @Autowired
  private MockMvc mvc;
  @Autowired
  private MongoTemplate template;

  @BeforeEach
  void beforeEach() {
    this.template.save(new User("user@company.com"), "users");
  }

  @Test
  void shouldReturnUser() throws Exception {
    this.mvc.perform(
            get("/api/authenticated"))
        .andExpect(handler().methodName("authenticated"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("user@company.com"))
        .andExpect(jsonPath("$.authorities").isArray())
        .andExpect(jsonPath("$.authorities[0]").value("ROLE_USER"));
  }
}
