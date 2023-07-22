package com.enofex.naikan.project.support;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.IfNull.ifNull;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.enofex.naikan.AbstractRepository;
import com.enofex.naikan.Filterable;
import com.enofex.naikan.FilterableCriteriaBuilder;
import com.enofex.naikan.ProjectId;
import com.enofex.naikan.model.Bom;
import com.enofex.naikan.project.ProjectFilter;
import com.enofex.naikan.project.ProjectFilter.FilterItem;
import com.enofex.naikan.project.ProjectRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
  public Optional<Bom> findById(ProjectId id) {
    return Optional.ofNullable(template().findById(id.id(), Bom.class, collectionName()));
  }

  @Override
  public boolean existsByProjectName(String projectName) {
    Query query = new Query(where("project.name").is(projectName));
    return template().exists(query, Bom.class, collectionName());
  }

  @Override
  public Bom update(ProjectId id, Bom bom) {
    Query query = new Query(where("id").is(id.id()));
    return findAndModify(query, false, bom);
  }

  @Override
  public Bom upsertByProjectName(Bom bom) {
    Query query = new Query(where("project.name").is(bom.project().name()));
    return findAndModify(query, true, bom);
  }

  private Bom findAndModify(Query query, boolean upsert, Bom bom) {
    Update update = Update.fromDocument(toDocument(bom), "deployments")
        .addToSet("deployments")
        .each(bom.deployments().all());

    FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true).upsert(upsert);

    return template().findAndModify(query, update, options, Bom.class, collectionName());
  }

  private Document toDocument(Bom bom) {
    Document document = new Document();
    template().getConverter().write(bom, document);

    return document;
  }
}

