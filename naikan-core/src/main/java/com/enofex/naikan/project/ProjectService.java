package com.enofex.naikan.project;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

  Page<Bom> findAll(Filterable filterable, Pageable pageable);

  Optional<Bom> findById(ProjectId id);

  Optional<BomDetail> findBomDetailById(ProjectId id);

  Page<Deployment> findDeployments(ProjectId id, Filterable filterable, Pageable pageable);

  Page<GroupedDeploymentsPerVersion> findGroupedDeploymentsPerVersion(ProjectId id,
      Filterable filterable, Pageable pageable);

  DeploymentsPerMonth findDeploymentsPerMonth(ProjectId id);

  List<LatestVersionPerEnvironment> findLatestVersionPerEnvironment(ProjectId id);

  Bom update(ProjectId id, Bom bom);

  Bom upsertByProjectName(Bom bom);

  boolean existsByProjectName(String projectName);

  ProjectFilter findFilter();
}
