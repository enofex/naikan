package com.enofex.naikan.project;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.previousOperation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.IfNull.ifNull;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.enofex.naikan.repository.AbstractRepository;
import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.web.FilterableCriteriaBuilder;
import com.enofex.naikan.model.Branch;
import com.enofex.naikan.model.Commit;
import com.enofex.naikan.model.Deployment;
import com.enofex.naikan.model.RepositoryTag;
import com.enofex.naikan.project.ProjectFilter.FilterItem;
import com.enofex.naikan.web.ProjectId;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Repository;

@Repository
class ProjectRepository extends AbstractRepository {

  ProjectRepository(MongoTemplate template) {
    super(template);
  }

  public Page<BomOverview> findAll(Filterable filterable, Pageable pageable) {
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
        .and(ArrayOperators.arrayOf(ifNull("$contacts")
            .then(Collections.emptyList())).length()
        ).as("contactsCount")
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
        .and("licenses").as("licenses")
        .and("deployments").as("deployments")
        .and("tags").as("tags")
        .and("repository").as("repository")

        .and("environments.name").as("environmentNames")
        .and("integrations.name").as("integrationNames")
        .and("developers.name").as("developerNames")
        .and("teams.name").as("teamNames")
        .and("contacts.name").as("contactNames")
        .and("technologies.name").as("technologyNames")
        .and("documentations.name").as("documentationNames")
        .and("licenses.name").as("licenseNames")
    );

    operations.add(AddFieldsOperation.builder()
        .addFieldWithValue("deploymentsPerMonth", null)
        .build()
    );

    operations.add(AddFieldsOperation.builder()
        .addFieldWithValue("commitsPerMonth", null)
        .build()
    );

    operations.add(AddFieldsOperation.builder()
        .addFieldWithValue(
            "lastDeployment",
            new Document(
                "$arrayElemAt",
                Arrays.asList("$deployments", -1)
            )
        )
        .build()
    );

    operations.add(AddFieldsOperation.builder()
        .addFieldWithValue("deploymentsEnvironmentsCount", new Document(
            "$size",
            new Document("$setUnion",
                Arrays.asList(
                    new Document("$ifNull",
                        Arrays.asList("$deployments.environment", Collections.emptyList())),
                    Collections.emptyList()
                )
            )
        ))
        .build()
    );

    operations.add(AddFieldsOperation.builder()
        .addFieldWithValue("deploymentsVersionsCount", new Document(
            "$size",
            new Document("$setUnion",
                Arrays.asList(
                    new Document("$ifNull",
                        Arrays.asList("$deployments.version", Collections.emptyList())),
                    Collections.emptyList()
                )
            )
        ))
        .build()
    );

    operations.add(AddFieldsOperation.builder()
        .addFieldWithValue("commitsCount", new Document(
            "$cond", Arrays.asList(
            new Document("$ifNull",
                Arrays.asList("$repository.totalCommits", null)),
            "$repository.totalCommits",
            0
        )
        ))
        .build()
    );

    operations.add(match(builder.toSearch(List.of(
        "tags", "project.name", "project.username", "project.version", "project.groupId",
        "project.artifactId", "project.repository", "project.packaging", "project.notes",
        "project.description", "integrations.name", "organization.name", "organization.department",
        "developers.name", "teams.name", "contacts.name", "technologies.name", "integrations.name",
        "documentations.name", "deployments.environment", "deployments.location"
    ))));

    operations.add(match(builder.toFilters()));

    return findAll(BomOverview.class, operations, pageable);
  }

  public ProjectFilter findFilter() {
    Aggregation aggregation = Aggregation.newAggregation(
        unwind("tags"),
        group("tags").count().as("count"),
        project("count").and("_id").as("name"),
        sort(Direction.DESC, "count")
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


  public Optional<BomDetail> findBomDetailById(ProjectId id) {
    List<AggregationOperation> operations = new ArrayList<>();

    operations.add(match(where("_id").is(id.id())));
    operations.add(AddFieldsOperation.builder()
        .addFieldWithValue("repository.totalCommits",
            new Document("$ifNull", Arrays.asList("$repository.totalCommits", 0))
        )
        .build()
    );
    operations.add(AddFieldsOperation.builder()
        .addFieldWithValue("repository.tagsCount",
            new Document("$size",
                new Document("$ifNull",
                    Arrays.asList(
                        "$repository.tags",
                        Collections.emptyList()
                    )
                )
            )
        )
        .build()
    );

    operations.add(AddFieldsOperation.builder()
        .addFieldWithValue("repository.branchesCount",
            new Document("$size",
                new Document("$ifNull",
                    Arrays.asList(
                        "$repository.branches",
                        Collections.emptyList()
                    )
                )
            )
        )
        .build()
    );

    return Optional.ofNullable(template()
        .aggregate(Aggregation.newAggregation(operations), collectionName(), BomDetail.class)
        .getUniqueMappedResult());
  }

  public Page<Deployment> findDeploymentsById(ProjectId id, Filterable filterable,
      Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = new ArrayList<>();

    operations.add(match(where("_id").is(id.id())));
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

  public Page<GroupedDeploymentsPerVersion> findGroupedDeploymentsPerVersionById(ProjectId id,
      Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = new ArrayList<>();

    operations.add(match(where("_id").is(id.id())));
    operations.add(unwind("deployments"));
    operations.add(group("deployments.version")
        .first("deployments.version").as("version")
        .push("deployments").as("deployments"));
    operations.add(project("version", "deployments")
        .and("deployments").size().as("count"));
    operations.add(sort(Direction.DESC, "version"));
    operations.add(match(builder.toSearch(List.of(
        "deployments.environment",
        "deployments.location",
        "deployments.version",
        "deployments.timestamp")))
    );

    operations.add(match(builder.toFilters()));

    return findAll(GroupedDeploymentsPerVersion.class, operations, pageable);
  }

  public CountsPerItems findDeploymentsPerMonthById(ProjectId id) {
    Aggregation aggregation = Aggregation.newAggregation(
        match(where("_id").is(id.id())),
        unwind("deployments"),
        project()
            .andExpression("dateToString('%Y-%m', deployments.timestamp)").as("month"),
        group("month").count().as("count"),
        sort(Direction.ASC, previousOperation(), "month"),
        project("count").and("month").previousOperation(),
        sort(Direction.ASC, previousOperation(), "month"),
        group()
            .push("month").as("names")
            .push("count").as("counts")
    );

    return countsPerMonth(aggregation);
  }

  public List<LatestVersionPerEnvironment> findLatestVersionPerEnvironmentById(ProjectId id) {
    Aggregation aggregation = Aggregation.newAggregation(
        match(where("_id").is(id.id())),
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

  public Page<Commit> findCommitsById(ProjectId id, Filterable filterable,
      Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = new ArrayList<>();

    operations.add(match(where("_id").is(id.id())));
    operations.add(unwind("repository.commits"));
    operations.add(project()
        .and("repository.commits.commitId").as("commitId")
        .and("repository.commits.timestamp").as("timestamp")
        .and("repository.commits.shortMessage").as("shortMessage")
        .and("repository.commits.author").as("author")
        .and("repository.commits.changes").as("changes"));

    operations.add(match(builder.toSearch(List.of(
        "commitId",
        "shortMessage",
        "timestamp",
        "author.name",
        "author.email")))
    );

    operations.add(match(builder.toFilters()));

    return findAll(Commit.class, operations, pageable);
  }

  public CountsPerItems findCommitsPerMonthById(ProjectId id) {
    Aggregation aggregation = Aggregation.newAggregation(
        match(where("_id").is(id.id())),
        unwind("repository.commits"),
        project()
            .andExpression("dateToString('%Y-%m', repository.commits.timestamp)").as("month"),
        group("month").count().as("count"),
        sort(Direction.ASC, previousOperation(), "month"),
        project("count").and("month").previousOperation(),
        sort(Direction.ASC, previousOperation(), "month"),
        group()
            .push("month").as("names")
            .push("count").as("counts")
    );

    return countsPerMonth(aggregation);
  }

  public Page<RepositoryTag> findRepositoryTagsById(ProjectId id, Filterable filterable,
      Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = new ArrayList<>();

    operations.add(match(where("_id").is(id.id())));
    operations.add(unwind("repository.tags"));
    operations.add(project()
        .and("repository.tags.name").as("name")
        .and("repository.tags.timestamp").as("timestamp"));

    operations.add(match(builder.toSearch(List.of(
        "name",
        "timestamp")))
    );

    operations.add(match(builder.toFilters()));

    return findAll(RepositoryTag.class, operations, pageable);
  }

  public Page<Branch> findRepositoryBranchesById(ProjectId id, Filterable filterable,
      Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = new ArrayList<>();

    operations.add(match(where("_id").is(id.id())));
    operations.add(unwind("repository.branches"));
    operations.add(project()
        .and("repository.branches.name").as("name"));

    operations.add(match(builder.toSearch(List.of(
        "name")))
    );

    operations.add(match(builder.toFilters()));

    return findAll(Branch.class, operations, pageable);
  }

  private CountsPerItems countsPerMonth(Aggregation aggregation) {
    CountsPerItems result = template().aggregate(
        aggregation, collectionName(), CountsPerItems.class).getUniqueMappedResult();

    if (result != null) {
      List<String> months = new ArrayList<>();
      List<Long> counts = new ArrayList<>();

      YearMonth currentMonth = YearMonth.now();
      YearMonth firstMonth = YearMonth.parse(result.names().getFirst());

      while (!firstMonth.isAfter(currentMonth)) {
        String formattedMonth = firstMonth.toString();
        months.add(formattedMonth);

        int index = result.names().indexOf(formattedMonth);
        if (index != -1) {
          counts.add(result.counts().get(index));
        } else {
          counts.add(0L);
        }

        firstMonth = firstMonth.plusMonths(1L);
      }

      return new CountsPerItems(months, counts);
    }

    return CountsPerItems.EMPTY;
  }

}

