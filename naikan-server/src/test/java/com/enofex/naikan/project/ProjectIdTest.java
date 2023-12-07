package com.enofex.naikan.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.enofex.naikan.web.ProjectId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProjectIdTest {

  @Test
  void shouldCreateProjectId() {
    String id = UUID.randomUUID().toString();
    ProjectId projectId = ProjectId.of(id);

    assertEquals(id, projectId.id());
  }
}
