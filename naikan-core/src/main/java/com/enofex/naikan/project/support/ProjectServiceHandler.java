package com.enofex.naikan.project.support;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.project.BomDetail;
import com.enofex.naikan.project.BomOverview;
import com.enofex.naikan.project.DeploymentsPerMonth;
import com.enofex.naikan.project.DeploymentsPerProject;
import com.enofex.naikan.project.GroupedDeploymentsPerVersion;
import com.enofex.naikan.project.LatestVersionPerEnvironment;
import com.enofex.naikan.project.ProjectFilter;
import com.enofex.naikan.project.ProjectRepository;
import com.enofex.naikan.project.ProjectService;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
class ProjectServiceHandler implements ProjectService {

  private final ProjectRepository projectRepository;

  ProjectServiceHandler(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  @Override
  public Page<BomOverview> findAll(Filterable filterable, Pageable pageable) {
    Page<BomOverview> all = this.projectRepository.findAll(filterable, pageable);

    return new PageImpl<>(
        all.getContent()
            .stream()
            .map(bomOverview -> BomOverview.of(bomOverview,
                this.findDeploymentsPerMonthById(ProjectId.of(bomOverview.id()))))
            .toList(),
        pageable,
        all.getTotalElements());
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
  public Page<Deployment> findDeploymentsById(ProjectId id, Filterable filterable,
      Pageable pageable) {
    return this.projectRepository.findDeploymentsById(id, filterable, pageable);
  }

  @Override
  public Page<GroupedDeploymentsPerVersion> findGroupedDeploymentsPerVersionById(ProjectId id,
      Filterable filterable, Pageable pageable) {
    return this.projectRepository.findGroupedDeploymentsPerVersionById(id, filterable, pageable);
  }

  @Override
  public DeploymentsPerMonth findDeploymentsPerMonthById(ProjectId id) {
    return this.projectRepository.findDeploymentsPerMonthById(id);
  }

  @Override
  public DeploymentsPerMonth findDeploymentsPerMonth(Filterable filterable, Pageable pageable) {
    Page<BomOverview> all = this.findAll(filterable, pageable);

    List<String> months = all.getContent()
        .stream()
        .flatMap(bomOverview -> bomOverview.deploymentsPerMonth().months().stream())
        .distinct()
        .sorted(Comparator.comparing(YearMonth::parse))
        .toList();

    Map<String, Long> monthCounts = new HashMap<>();

    all.getContent().forEach(bomOverview -> {
      List<String> bomMonths = bomOverview.deploymentsPerMonth().months();
      List<Long> bomCounts = bomOverview.deploymentsPerMonth().counts();

      for (int i = 0; i < bomMonths.size(); i++) {
        String month = bomMonths.get(i);
        Long count = bomCounts.get(i);
        monthCounts.put(month, monthCounts.getOrDefault(month, 0L) + count);
      }
    });

    List<Long> counts = months.stream()
        .map(monthCounts::get)
        .toList();

    return new DeploymentsPerMonth(months, counts);
  }


  @Override
  public DeploymentsPerProject findDeploymentsPerProject(Filterable filterable,
      Pageable pageable) {
    Page<BomOverview> all = this.findAll(filterable, pageable);

    return new DeploymentsPerProject(
        all.getContent().stream().map(bomOverview -> bomOverview.project().name()).toList(),
        all.getContent().stream().map(BomOverview::deploymentsCount).toList()
    );
  }

  @Override
  public List<LatestVersionPerEnvironment> findLatestVersionPerEnvironmentById(ProjectId id) {
    return this.projectRepository.findLatestVersionPerEnvironmentById(id);
  }
}
