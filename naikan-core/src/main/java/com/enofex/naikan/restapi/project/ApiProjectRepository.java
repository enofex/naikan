package com.enofex.naikan.restapi.project;

import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import java.util.Optional;

public interface ApiProjectRepository {

  Optional<Bom> findById(ProjectId id);

  Bom updateById(ProjectId id, Bom bom);

  Bom upsertByProjectName(Bom bom);

  boolean existsByProjectName(String projectName);
}
