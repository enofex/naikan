package com.enofex.naikan.project.support;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.project.BomDetail;
import com.enofex.naikan.project.DeploymentsPerMonth;
import com.enofex.naikan.project.GroupedDeploymentsPerVersion;
import com.enofex.naikan.project.LatestVersionPerEnvironment;
import com.enofex.naikan.project.ProjectFilter;
import com.enofex.naikan.project.ProjectRepository;
import com.enofex.naikan.project.ProjectService;
import java.util.List;
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
  public Optional<BomDetail> findBomDetailById(ProjectId id) {
    return this.projectRepository.findBomDetailById(id);
  }

  @Override
  public Page<Deployment> findDeployments(ProjectId id, Filterable filterable, Pageable pageable) {
    return this.projectRepository.findDeployments(id, filterable, pageable);
  }

  @Override
  public Page<GroupedDeploymentsPerVersion> findGroupedDeploymentsPerVersion(ProjectId id,
      Filterable filterable, Pageable pageable) {
    return this.projectRepository.findGroupedDeploymentsPerVersion(id, filterable, pageable);
  }

  @Override
  public DeploymentsPerMonth findDeploymentsPerMonth(ProjectId id) {
    return this.projectRepository.findDeploymentsPerMonth(id);
  }

  @Override
  public List<LatestVersionPerEnvironment> findLatestVersionPerEnvironment(ProjectId id) {
    return this.projectRepository.findLatestVersionPerEnvironment(id);
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
