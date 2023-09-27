package com.enofex.naikan.project;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Branch;
import com.enofex.naikan.model.Commit;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.model.RepositoryTag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

  Page<BomOverview> findAll(Filterable filterable, Pageable pageable);

  ProjectFilter findFilter();

  Optional<Bom> findById(ProjectId id);

  Optional<BomDetail> findBomDetailById(ProjectId id);

  Page<Deployment> findDeploymentsById(ProjectId id, Filterable filterable, Pageable pageable);

  Page<GroupedDeploymentsPerVersion> findGroupedDeploymentsPerVersionById(ProjectId id,
      Filterable filterable, Pageable pageable);

  CountsPerItems findDeploymentsPerMonthById(ProjectId id);

  CountsPerItems findDeploymentsPerMonth(Filterable filterable, Pageable pageable);

  CountsPerItems findDeploymentsPerProject(Filterable filterable, Pageable pageable);

  List<LatestVersionPerEnvironment> findLatestVersionPerEnvironmentById(ProjectId id);

  Page<Commit> findCommitsById(ProjectId id, Filterable filterable, Pageable pageable);

  CountsPerItems findCommitsPerMonthById(ProjectId id);

  CountsPerItems findCommitsPerMonth(Filterable filterable, Pageable pageable);

  CountsPerItems findCommitsPerProject(Filterable filterable, Pageable pageable);

  Page<RepositoryTag> findRepositoryTagsById(ProjectId id, Filterable filterable, Pageable pageable);

  Page<Branch> findRepositoryBranchesById(ProjectId id, Filterable filterable, Pageable pageable);
}
