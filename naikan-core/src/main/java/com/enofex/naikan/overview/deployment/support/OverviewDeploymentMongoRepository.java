package com.enofex.naikan.overview.deployment.support;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.ROOT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.FilterableCriteriaBuilder;
import com.enofex.naikan.overview.OverviewRepository;
import com.enofex.naikan.overview.OverviewTopGroups;
import com.enofex.naikan.overview.deployment.OverviewDeployment;
import com.enofex.naikan.overview.deployment.OverviewDeploymentRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Repository;

@Repository
class OverviewDeploymentMongoRepository extends OverviewRepository implements
    OverviewDeploymentRepository {

  OverviewDeploymentMongoRepository(MongoTemplate template) {
    super(template);
  }

  @Override
  public Page<OverviewDeployment> findAll(Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);
    List<AggregationOperation> operations = new ArrayList<>();

    operations.add(unwind("deployments"));
    operations.add(project()
        .and("deployments").as("deployment")
        .and(ROOT).as("bom"));

    operations.add(match(builder.toSearch(List.of(
        "deployment.environment", "deployment.location", "deployment.version",
        "deployment.timestamp", "bom.project.name", "bom.project.description",
        "bom.project.repository"))));

    operations.add(match(builder.toFilters()));

    return findAll(OverviewDeployment.class, operations, pageable);
  }

  @Override
  public OverviewTopGroups findTopProjects(long topN) {
    Aggregation aggregation = newAggregation(
        unwind("deployments"),
        group("project.name").count().as("count"),
        project("count").and("project.name").previousOperation(),
        sort(Sort.Direction.DESC, "count"),
        limit(topN),
        group().push("project.name").as("names").push("count").as("counts"),
        project("names", "counts")
    );

    return template().aggregate(aggregation, collectionName(), OverviewTopGroups.class)
        .getUniqueMappedResult();
  }

}