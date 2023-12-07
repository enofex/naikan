package com.enofex.naikan.restapi.project;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.web.ProjectId;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class ApiProjectService {

  private final ApiProjectRepository apiProjectRepository;

  ApiProjectService(ApiProjectRepository apiProjectRepository) {
    this.apiProjectRepository = apiProjectRepository;
  }

  @Transactional(readOnly = true)
  public Optional<Bom> findById(ProjectId id) {
    return this.apiProjectRepository.findById(id);
  }

  public Bom updateById(ProjectId id, Bom bom) {
    return this.apiProjectRepository.updateById(id, bom);
  }

  public Bom upsertByProjectName(Bom bom) {
    return this.apiProjectRepository.upsertByProjectName(bom);
  }

  @Transactional(readOnly = true)
  public boolean existsByProjectName(String projectName) {
    return this.apiProjectRepository.existsByProjectName(projectName);
  }

}
