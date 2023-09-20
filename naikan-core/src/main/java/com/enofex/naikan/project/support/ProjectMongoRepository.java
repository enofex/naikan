package com.enofex.naikan.project.support;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.previousOperation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.IfNull.ifNull;

import com.enofex.naikan.AbstractRepository;
import com.enofex.naikan.Filterable;
import com.enofex.naikan.FilterableCriteriaBuilder;
import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.project.BomDetail;
import com.enofex.naikan.project.DeploymentsPerMonth;
import com.enofex.naikan.project.GroupedDeploymentsPerVersion;
import com.enofex.naikan.project.LatestVersionPerEnvironment;
import com.enofex.naikan.project.ProjectFilter;
import com.enofex.naikan.project.ProjectFilter.FilterItem;
import com.enofex.naikan.project.ProjectRepository;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

@Repository
class ProjectMongoRepository extends AbstractRepository implements ProjectRepository {

  ProjectMongoRepository(MongoTemplate template) {
    super(template);
  }

  @Override
  public Page<Bom> findAll(Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = new ArrayList<>();

    operations.add(project()
        .and("bomFormat").as("bomFormat")
        .and("specVersion").as("specVersion")
        .and("timestamp").as("timestamp")

        .and(ArrayOperators.arrayOf(ifNull("$environments")
            .then(Collections.emptyList())).length()
        ).as("environmentsCount")
        .and(ArrayOperators.arrayOf(ifNull("$teams")
            .then(Collections.emptyList())).length()
        ).as("teamsCount")
        .and(ArrayOperators.arrayOf(ifNull("$developers")
            .then(Collections.emptyList())).length()
        ).as("developersCount")
        .and(ArrayOperators.arrayOf(ifNull("$documentations")
            .then(Collections.emptyList())).length()
        ).as("documentationsCount")
        .and(ArrayOperators.arrayOf(ifNull("$integrations")
            .then(Collections.emptyList())).length()
        ).as("integrationsCount")
        .and(ArrayOperators.arrayOf(ifNull("$technologies")
            .then(Collections.emptyList())).length()
        ).as("technologiesCount")
        .and(ArrayOperators.arrayOf(ifNull("$deployments")
            .then(Collections.emptyList())).length()
        ).as("deploymentsCount")
        .and(ArrayOperators.arrayOf(ifNull("$licenses")
            .then(Collections.emptyList())).length()
        ).as("licensesCount")

        .and("project").as("project")
        .and("organization").as("organization")
        .and("environments").as("environments")
        .and("integrations").as("integrations")
        .and("developers").as("developers")
        .and("teams").as("teams")
        .and("contacts").as("contacts")
        .and("technologies").as("technologies")
        .and("documentations").as("documentations")
        .and("deployments").as("deployments")
        .and("licenses").as("licenses")
        .and("tags").as("tags")
    );

    operations.add(match(builder.toSearch(List.of(
        "tags", "project.name", "project.username", "project.version", "project.groupId",
        "project.artifactId", "project.repository", "project.packaging", "project.notes",
        "project.description", "integrations.name", "organization.name", "organization.department",
        "developers.name", "teams.name", "contacts.name", "technologies.name", "integrations.name",
        "documentations.name", "deployments.environment", "deployments.location"
    ))));

    operations.add(match(builder.toFilters()));

    return findAll(Bom.class, operations, pageable);
  }

  @Override
  public ProjectFilter findFilter() {
    Aggregation aggregation = Aggregation.newAggregation(
        unwind("tags"),
        group("tags").count().as("count"),
        project("count").and("_id").as("name"),
        sort(Sort.Direction.DESC, "count")
    );

    List<FilterItem> tags = template().aggregate(aggregation, collectionName(), FilterItem.class)
        .getMappedResults();
    List<FilterItem> packaging = filterItems("project", "packaging");
    List<FilterItem> groupIds = filterItems("project", "groupId");
    List<FilterItem> organizations = filterItems("organization", "name");
    List<FilterItem> organizationDepartments = filterItems("organization", "department");
    List<FilterItem> environments = filterArrayItems("environments", "name");
    List<FilterItem> environmentLocations = filterArrayItems("environments", "location");
    List<FilterItem> environmentTags = filterArrayArrayItems("environments", "tags");
    List<FilterItem> teams = filterArrayItems("teams", "name");
    List<FilterItem> developers = filterArrayItems("developers", "name");
    List<FilterItem> developerOrganizations = filterArrayItems("developers", "organization");
    List<FilterItem> developerDepartments = filterArrayItems("developers", "department");
    List<FilterItem> developerRoles = filterArrayArrayItems("developers", "roles");
    List<FilterItem> contacts = filterArrayItems("contacts", "name");
    List<FilterItem> contactRoles = filterArrayArrayItems("contacts", "roles");
    List<FilterItem> integrations = filterArrayItems("integrations", "name");
    List<FilterItem> integrationTags = filterArrayArrayItems("integrations", "tags");
    List<FilterItem> technologies = filterArrayItems("technologies", "name");
    List<FilterItem> technologyVersions = filterArrayItems("technologies", "version");
    List<FilterItem> technologyTags = filterArrayArrayItems("technologies", "tags");
    List<FilterItem> deployments = filterArrayItems("deployments", "location");
    List<FilterItem> licenses = filterArrayItems("licenses", "name");

    return new ProjectFilter(packaging, tags, groupIds, organizations, organizationDepartments,
        environments, environmentLocations, environmentTags, teams, developers,
        developerOrganizations, developerDepartments, developerRoles, contacts, contactRoles,
        integrations, integrationTags, technologies, technologyVersions, technologyTags,
        deployments, licenses
    );
  }

  private List<FilterItem> filterItems(String field, String property) {
    Aggregation aggregation = Aggregation.newAggregation(
        group(field + "." + property).count().as("count"),
        project("count").and("_id").as("name"),
        sort(Direction.DESC, "count")
    );

    return template().aggregate(aggregation, collectionName(), FilterItem.class)
        .getMappedResults();
  }

  private List<FilterItem> filterArrayItems(String field, String property) {
    Aggregation aggregation = Aggregation.newAggregation(
        unwind(field),
        group(field + "." + property).count().as("count"),
        project("count").and("_id").as("name"),
        sort(Direction.DESC, "count")
    );

    return template().aggregate(aggregation, collectionName(), FilterItem.class)
        .getMappedResults();
  }

  private List<FilterItem> filterArrayArrayItems(String field, String property) {
    Aggregation aggregation = Aggregation.newAggregation(
        unwind(field),
        Aggregation.unwind(field + "." + property),
        group(field + "." + property).count().as("count"),
        project("count").and("_id").as("name"),
        sort(Direction.DESC, "count")
    );

    return template().aggregate(aggregation, collectionName(), FilterItem.class)
        .getMappedResults();
  }



  @Override
  public Optional<BomDetail> findBomDetailById(ProjectId id) {
    return Optional.ofNullable(template().findById(id.id(), BomDetail.class, collectionName()));
  }

  @Override
  public Page<Deployment> findDeployments(ProjectId id, Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = new ArrayList<>();

    operations.add(match(Criteria.where("_id").is(id.id())));
    operations.add(unwind("deployments"));
    operations.add(project()
        .and("deployments.environment").as("environment")
        .and("deployments.location").as("location")
        .and("deployments.version").as("version")
        .and("deployments.timestamp").as("timestamp"));

    operations.add(match(builder.toSearch(List.of(
        "environment",
        "location",
        "version",
        "timestamp")))
    );

    operations.add(match(builder.toFilters()));

    return findAll(Deployment.class, operations, pageable);
  }

  @Override
  public Page<GroupedDeploymentsPerVersion> findGroupedDeploymentsPerVersion(ProjectId id,
      Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = new ArrayList<>();

    operations.add(match(Criteria.where("_id").is(id.id())));
    operations.add(unwind("deployments"));
    operations.add(group("deployments.version")
        .first("deployments.version").as("version")
        .push("deployments").as("deployments"));
    operations.add(project("version", "deployments")
        .and("deployments").size().as("count"));
    operations.add(sort(Sort.Direction.DESC, "version"));
    operations.add(match(builder.toSearch(List.of(
        "deployments.environment",
        "deployments.location",
        "deployments.version",
        "deployments.timestamp")))
    );

    operations.add(match(builder.toFilters()));

    return findAll(GroupedDeploymentsPerVersion.class, operations, pageable);
  }

  @Override
  public DeploymentsPerMonth findDeploymentsPerMonth(ProjectId id) {
    Aggregation aggregation = Aggregation.newAggregation(
        match(Criteria.where("_id").is(id.id())),
        unwind("deployments"),
        project()
            .andExpression("dateToString('%Y-%m', deployments.timestamp)").as("month"),
        group("month").count().as("count"),
        sort(Sort.Direction.ASC, previousOperation(), "month"),
        project("count").and("month").previousOperation(),
        sort(Sort.Direction.ASC, previousOperation(), "month"),
        group()
            .push("month").as("months")
            .push("count").as("counts")
    );

    DeploymentsPerMonth result = template().aggregate(
        aggregation, collectionName(), DeploymentsPerMonth.class).getUniqueMappedResult();

    if (result != null) {
      List<String> months = new ArrayList<>();
      List<Integer> counts = new ArrayList<>();

      YearMonth currentMonth = YearMonth.now();
      YearMonth firstMonth = YearMonth.parse(result.months().get(0));

      while (!firstMonth.isAfter(currentMonth)) {
        String formattedMonth = firstMonth.toString();
        months.add(formattedMonth);

        int index = result.months().indexOf(formattedMonth);
        if (index != -1) {
          counts.add(result.counts().get(index));
        } else {
          counts.add(0);
        }

        firstMonth = firstMonth.plusMonths(1L);
      }

      return new DeploymentsPerMonth(months, counts);
    }

    return new DeploymentsPerMonth(Collections.emptyList(), Collections.emptyList());
  }

  @Override
  public List<LatestVersionPerEnvironment> findLatestVersionPerEnvironment(ProjectId id) {
    Aggregation aggregation = Aggregation.newAggregation(
        match(Criteria.where("_id").is(id.id())),
        unwind("deployments"),
        sort(Sort.by(
            Sort.Order.asc("deployments.environment"),
            Sort.Order.desc("deployments.timestamp"))),
        group("deployments.environment")
            .first("deployments").as("latestDeployment")
            .max("deployments.timestamp").as("latestTimestamp"),
        sort(Sort.by(Sort.Order.desc("latestTimestamp"))),
        project()
            .and("latestDeployment").as("deployment")
            .and("latestDeployment.environment").as("environment")
    );
    return template().aggregate(aggregation, collectionName(),
        LatestVersionPerEnvironment.class).getMappedResults();
  }
}

