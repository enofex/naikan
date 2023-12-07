package com.enofex.naikan.overview.deployment;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.ROOT;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.web.FilterableCriteriaBuilder;
import com.enofex.naikan.overview.OverviewRepository;
import com.enofex.naikan.overview.OverviewTopGroups;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Repository;

@Repository
class OverviewDeploymentRepository extends OverviewRepository {

  OverviewDeploymentRepository(MongoTemplate template) {
    super(template);
  }

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


  public OverviewTopGroups findTopProjects(long topN) {
    Aggregation aggregation = Aggregation.newAggregation(
        unwind("deployments"),
        group("project.name").count().as("count"),
        sort(Direction.DESC, "count").and(Direction.ASC, "project.name"),
        limit(topN),
        group()
            .push("_id").as("names")
            .push("count").as("counts"),
        project()
            .and("names").as("names")
            .and("counts").as("counts")
    );

    return template().aggregate(aggregation, collectionName(), OverviewTopGroups.class)
        .getUniqueMappedResult();
  }

}