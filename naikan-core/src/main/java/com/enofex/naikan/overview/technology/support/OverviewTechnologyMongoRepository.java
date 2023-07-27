package com.enofex.naikan.overview.technology.support;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import com.enofex.naikan.Filterable;
import com.enofex.naikan.FilterableCriteriaBuilder;
import com.enofex.naikan.overview.OverviewRepository;
import com.enofex.naikan.overview.OverviewTopGroups;

import com.enofex.naikan.overview.technology.OverviewTechnologyRepository;

import com.enofex.naikan.overview.technology.TechnologyGroup;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Repository;

@Repository
class OverviewTechnologyMongoRepository extends OverviewRepository implements
    OverviewTechnologyRepository {

  OverviewTechnologyMongoRepository(MongoTemplate template) {
    super(template);
  }

  @Override
  public Page<TechnologyGroup> findAll(Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);

    List<AggregationOperation> operations = defaultOverviewGroupOperations(
        "technologies",
        List.of("technologies.name", "technologies.version"),
        builder.toSearch(List.of("group.name", "group.version")),
        builder.toFilters());

    return findAll(TechnologyGroup.class, operations, pageable);
  }

  @Override
  public OverviewTopGroups findTopTechnologies(long topN) {
    Aggregation aggregation = newAggregation(
        unwind("technologies"),
        group("technologies.name").count().as("count"),
        project("count").and("technologies.name").previousOperation(),
        sort(Sort.Direction.DESC, "count"),
        group().push("technologies.name").as("names").push("count").as("counts"),
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
