package com.enofex.naikan.restapi.project.support;

import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.restapi.project.ApiProjectRepository;
import com.enofex.naikan.restapi.project.ApiProjectService;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class ApiProjectServiceHandler implements ApiProjectService {

  private final ApiProjectRepository apiProjectRepository;

  ApiProjectServiceHandler(ApiProjectRepository apiProjectRepository) {
    this.apiProjectRepository = apiProjectRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Bom> findById(ProjectId id) {
    return this.apiProjectRepository.findById(id);
  }

  @Override
  public Bom update(ProjectId id, Bom bom) {
    return this.apiProjectRepository.update(id, bom);
  }

  @Override
  public Bom upsertByProjectName(Bom bom) {
    return this.apiProjectRepository.upsertByProjectName(bom);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsByProjectName(String projectName) {
    return this.apiProjectRepository.existsByProjectName(projectName);
  }

}
