package com.enofex.naikan.project;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectRepository {

  Page<Bom> findAll(Filterable filterable, Pageable pageable);

  Optional<Bom> findById(ProjectId id);

  Bom update(ProjectId id, Bom bom);

  Bom upsertByProjectName(Bom bom);

  boolean existsByProjectName(String projectName);

  ProjectFilter findFilter();
}
