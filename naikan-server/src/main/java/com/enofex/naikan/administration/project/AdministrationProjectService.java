package com.enofex.naikan.administration.project;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.web.ProjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
class AdministrationProjectService {

  private final AdministrationProjectRepository administrationProjectRepository;

  AdministrationProjectService(AdministrationProjectRepository administrationProjectRepository) {
    this.administrationProjectRepository = administrationProjectRepository;
  }

  public Page<Project> findAll(Filterable filterable, Pageable pageable) {
    return this.administrationProjectRepository.findAll(filterable, pageable);
  }

  @Transactional
  public long deleteById(ProjectId id) {
    return this.administrationProjectRepository.deleteById(id);
  }
}
