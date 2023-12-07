package com.enofex.naikan.overview.technology;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.unwind;

import com.enofex.naikan.web.Filterable;
import com.enofex.naikan.web.FilterableCriteriaBuilder;
import com.enofex.naikan.overview.OverviewRepository;
import com.enofex.naikan.overview.OverviewTopGroups;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.stereotype.Repository;

@Repository
class OverviewTechnologyRepository extends OverviewRepository {

  OverviewTechnologyRepository(MongoTemplate template) {
    super(template);
  }

  public Page<TechnologyGroup> findAll(Filterable filterable, Pageable pageable) {
    FilterableCriteriaBuilder builder = new FilterableCriteriaBuilder(filterable);

    List<AggregationOperation> operations = defaultOverviewGroupOperations(
        "technologies",
        List.of("technologies.name", "technologies.version"),
        builder.toSearch(List.of("group.name", "group.version")),
        builder.toFilters());

    return findAll(TechnologyGroup.class, operations, pageable);
  }

  public OverviewTopGroups findTopTechnologies(long topN) {
    Aggregation aggregation = Aggregation.newAggregation(
        unwind("technologies"),
        group("technologies.name").count().as("count"),
        sort(Direction.DESC, "count").and(Direction.ASC, "_id"),
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
