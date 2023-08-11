package com.enofex.naikan.administration.project.support;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.ProjectId;
import com.enofex.naikan.administration.project.AdministrationProjectRepository;
import com.enofex.naikan.administration.project.AdministrationProjectService;
import com.enofex.naikan.administration.project.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class AdministrationProjectServiceHandler implements AdministrationProjectService {

  private final AdministrationProjectRepository administrationRepository;

  AdministrationProjectServiceHandler(AdministrationProjectRepository administrationRepository) {
    this.administrationRepository = administrationRepository;
  }

  @Override
  public Page<Project> findAll(Filterable filterable, Pageable pageable) {
    return administrationRepository.findAll(filterable, pageable);
  }

  @Override
  public long delete(ProjectId id) {
    return administrationRepository.delete(id);
  }
}
