package com.enofex.naikan.overview.environment.support;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.FilterableCriteriaBuilder;
import com.enofex.naikan.overview.OverviewGroup;
import com.enofex.naikan.overview.OverviewRepository;
import com.enofex.naikan.overview.OverviewTopGroups;
import com.enofex.naikan.overview.environment.OverviewEnvironmentRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Repository;

@Repository
class OverviewEnvironmentMongoRepository extends OverviewRepository implements
    OverviewEnvironmentRepository {

  OverviewEnvironmentMongoRepository(MongoTemplate template) {
    super(template);
  }

  @Override
  public Page<OverviewGroup> findAll(Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);

    List<AggregationOperation> operations = defaultOverviewGroupOperations(
        "environments",
        List.of("environments.name"),
        builder.toSearch(List.of("group.name")),
        builder.toFilters());

    return findAll(OverviewGroup.class, operations, pageable);
  }

  @Override
  public OverviewTopGroups findTopEnvironments(long topN) {
    Aggregation aggregation = newAggregation(
        unwind("environments"),
        group("environments.name").count().as("count"),
        project("count").and("environments.name").previousOperation(),
        sort(Sort.Direction.DESC, "count"),
        group().push("environments.name").as("names").push("count").as("counts"),
        project("names", "counts"),
        unwind("names"),
        sort(Sort.Direction.DESC, "counts"),
        sort(Sort.Direction.ASC, "names"),
        limit(topN),
        group().push("names").as("names").first("counts").as("counts")
    );

    return template().aggregate(aggregation, collectionName(), OverviewTopGroups.class)
        .getUniqueMappedResult();
  }

}
