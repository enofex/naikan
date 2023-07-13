package com.enofex.naikan.administration.project;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.ProjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdministrationProjectRepository {

  Page<Project> findAll(Filterable filterable, Pageable pageable);

  long delete(ProjectId id);
}
