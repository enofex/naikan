package com.enofex.naikan.project.support;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.project.ProjectFilter;
import com.enofex.naikan.project.ProjectRepository;
import com.enofex.naikan.project.ProjectService;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class ProjectServiceHandler implements ProjectService {

  private final ProjectRepository projectRepository;

  ProjectServiceHandler(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  @Override
  public Page<Bom> findAll(Filterable filterable, Pageable pageable) {
    return this.projectRepository.findAll(filterable, pageable);
  }

  @Override
  public ProjectFilter findFilter() {
    return this.projectRepository.findFilter();
  }

  @Override
  public Optional<Bom> findById(ProjectId id) {
    return this.projectRepository.findById(id);
  }

  @Override
  public boolean existsByProjectName(String projectName) {
    return this.projectRepository.existsByProjectName(projectName);
  }

  @Override
  public Bom update(ProjectId id, Bom bom) {
    return this.projectRepository.update(id, bom);
  }

  @Override
  public Bom upsertByProjectName(Bom bom) {
    return this.projectRepository.upsertByProjectName(bom);
  }

}
