package com.enofex.naikan.restapi.project;

import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import java.util.Optional;

public interface ApiProjectService {

  Optional<Bom> findById(ProjectId id);

  Bom update(ProjectId id, Bom bom);

  Bom upsertByProjectName(Bom bom);

  boolean existsByProjectName(String projectName);
}
