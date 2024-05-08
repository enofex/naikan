package com.enofex.naikan.project;

import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Branch;
import com.enofex.naikan.model.Commit;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.model.RepositoryTag;
import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.web.ProjectId;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class ProjectService {

  private final ProjectRepository projectRepository;

  ProjectService(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  public Page<BomOverview> findAll(Filterable filterable, Pageable pageable) {
    Page<BomOverview> all = this.projectRepository.findAll(filterable, pageable);

    return new PageImpl<>(
        all.getContent()
            .stream()
            .map(bomOverview -> BomOverview.of(
                bomOverview,
                this.findDeploymentsPerMonthById(ProjectId.of(bomOverview.id())),
                this.findCommitsPerMonthById(ProjectId.of(bomOverview.id()))))
            .toList(),
        pageable,
        all.getTotalElements());
  }

  public ProjectFilter findFilter() {
    return this.projectRepository.findFilter();
  }

  public Optional<Bom> findById(ProjectId id) {
    return this.projectRepository.findById(id);
  }

  public Optional<BomDetail> findBomDetailById(ProjectId id) {
    return this.projectRepository.findBomDetailById(id);
  }

  public Page<Deployment> findDeploymentsById(ProjectId id, Filterable filterable,
      Pageable pageable) {
    return this.projectRepository.findDeploymentsById(id, filterable, pageable);
  }

  public Page<GroupedDeploymentsPerVersion> findGroupedDeploymentsPerVersionById(ProjectId id,
      Filterable filterable, Pageable pageable) {
    return this.projectRepository.findGroupedDeploymentsPerVersionById(id, filterable, pageable);
  }

  public CountsPerItems findDeploymentsPerMonthById(ProjectId id) {
    return this.projectRepository.findDeploymentsPerMonthById(id);
  }

  public CountsPerItems findDeploymentsPerMonth(Filterable filterable, Pageable pageable) {
    return countsPerMonth(filterable, pageable, BomOverview::deploymentsPerMonth);
  }

  public CountsPerItems findDeploymentsPerProject(Filterable filterable,
      Pageable pageable) {
    Page<BomOverview> all = this.findAll(filterable, pageable);

    return new CountsPerItems(
        all.getContent().stream().map(bomOverview -> bomOverview.project().name()).toList(),
        all.getContent().stream().map(BomOverview::deploymentsCount).toList()
    );
  }

  public List<LatestVersionPerEnvironment> findLatestVersionPerEnvironmentById(ProjectId id) {
    return this.projectRepository.findLatestVersionPerEnvironmentById(id);
  }

  public Page<Commit> findCommitsById(ProjectId id, Filterable filterable,
      Pageable pageable) {
    return this.projectRepository.findCommitsById(id, filterable, pageable);
  }

  public CountsPerItems findCommitsPerMonthById(ProjectId id) {
    return this.projectRepository.findCommitsPerMonthById(id);
  }

  public CountsPerItems findCommitsPerMonth(Filterable filterable, Pageable pageable) {
    return countsPerMonth(
        filterable,
        pageable,
        BomOverview::commitsPerMonth);
  }

  public CountsPerItems findCommitsPerProject(Filterable filterable, Pageable pageable) {
    Page<BomOverview> all = this.findAll(filterable, pageable);

    return new CountsPerItems(
        all.getContent().stream().map(bomOverview -> bomOverview.project().name()).toList(),
        all.getContent().stream().map(BomOverview::commitsCount).toList()
    );
  }

  public Page<RepositoryTag> findRepositoryTagsById(ProjectId id, Filterable filterable,
      Pageable pageable) {
    return this.projectRepository.findRepositoryTagsById(id, filterable, pageable);
  }

  public Page<Branch> findRepositoryBranchesById(ProjectId id, Filterable filterable,
      Pageable pageable) {
    return this.projectRepository.findRepositoryBranchesById(id, filterable, pageable);
  }

  private CountsPerItems countsPerMonth(Filterable filterable, Pageable pageable,
      Function<BomOverview, CountsPerItems> function) {
    Page<BomOverview> all = this.findAll(filterable, pageable);
    if (all.getContent().isEmpty()) {
      return CountsPerItems.EMPTY;
    }

    List<String> months = all.getContent()
        .stream()
        .flatMap(bomOverview -> function.apply(bomOverview).names().stream())
        .distinct()
        .sorted(Comparator.comparing(YearMonth::parse))
        .toList();

    Map<String, Long> monthCounts = new HashMap<>();

    all.getContent().forEach(bomOverview -> {
      List<String> bomMonths = function.apply(bomOverview).names();
      List<Long> bomCounts = function.apply(bomOverview).counts();

      for (int i = 0, size = bomMonths.size(); i < size; i++) {
        String month = bomMonths.get(i);
        Long count = bomCounts.get(i);
        monthCounts.put(month, monthCounts.getOrDefault(month, 0L) + count);
      }
    });

    List<Long> counts = months.stream()
        .map(monthCounts::get)
        .toList();

    return new CountsPerItems(months, counts);
  }
}
